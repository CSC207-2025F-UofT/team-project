"""用 LightGBM 预测公式图片的最佳缩放系数。

formula_layout.py 按"最高行带"定缩放：scale = 112 / 最高行带高度。行带切得不对时（手写斜行、
大括号或根号把几行连成一个带、分式被切开等），缩放就不合适。本模块预测一个乘在规则最高行带上的
系数 m（即 load_formula_image / render 的 band_scale）：实际缩放 = 112 / (最高行带 × m)。
m < 1 字放大，m > 1 字缩小，m = 1 就是原规则。

"最佳"由识别模型自己决定：对每张没参与训练的验证图，依次试 CANDIDATES 里的每个 m，用 teacher
forcing 算标签序列的平均 token 负对数似然（NLL），NLL 越低说明模型在这个缩放下越容易读对。
特征只来自裁剪后的二值笔迹图，和运行时能拿到的信息一样，不看来源也不看标签。

用法（在 src/neutral network/ 下，用装了 torch、lightgbm、scipy 的 .venv）：
  python crop.py label              # 跑识别模型，生成每张图在各候选 m 下的 NLL 表
  python crop.py fit                # 交叉验证评估各方法，并在全部数据上训练最终模型
  python crop.py predict a.png ...  # 预测图片的 m
"""
import argparse
import json
import math
import random
import time
from collections import Counter
from pathlib import Path

import numpy as np
import torch
import torch.nn.functional as F
from scipy import ndimage, signal

from formula_layout import FLAT_FRACTION, GAP_MIN_PX, band_heights, formula_layout, row_spans, tallest_band
from model import load_training_weights, load_vocab, max_len, pad_id, vital_mini
from preprocess import formula_ink_mask, load_formula_image
from train import IMAGE_INVERT, MathWriting, collate, convert, read_strokes, render

HERE = Path(__file__).resolve().parent
CHECKPOINTS = HERE.parents[1] / "checkpoints"
OUT_DIR = CHECKPOINTS / "crop"
TABLE_PATH = OUT_DIR / "labels.npz"
MODEL_PATH = OUT_DIR / "crop_lgbm.txt"

CANDIDATES = tuple(2.0 ** (k / 4) for k in range(-6, 9))   # m: 0.35 ... 4，按 2^(1/4) 递增
LOG2_CANDIDATES = np.log2(CANDIDATES)
RULE_INDEX = CANDIDATES.index(1.0)
GAP_FRACTIONS = (0.005, 0.01, 0.02, 0.03, 0.05, 0.08)       # 多阈值行带
SHEARS = tuple(k * 0.05 for k in range(-4, 5))               # 倾斜校正试的剪切系数，含精确的 0
MAX_SHEAR_PIXELS = 200_000                                   # 剪切投影最多用这么多笔迹像素
SOFT_TAU = 0.02                                              # direct 方法的软 argmin 温度（NLL 单位）
LGB_PARAMS = dict(objective="regression", learning_rate=0.05, num_leaves=31, min_data_in_leaf=40,
                  feature_fraction=0.8, bagging_fraction=0.8, bagging_freq=1, lambda_l2=1.0,
                  verbose=-1, seed=0, num_threads=0)
LGB_ROUNDS = 300


# ---------- 特征 ----------
def ink_features(mask: np.ndarray) -> dict[str, float]:
    """裁剪后的二值笔迹图 [H,W] -> 特征。长度都除以笔迹高或宽，和分辨率无关。"""
    mask = np.asarray(mask, dtype=bool)
    h, w = mask.shape
    ink = max(int(mask.sum()), 1)
    spans = row_spans(mask.any(axis=1))
    rule = max(tallest_band(spans, h, GAP_MIN_PX), FLAT_FRACTION * w)
    f = {"log_aspect": math.log(w / h), "density": ink / (h * w), "rule_band": rule / h}

    for g in GAP_FRACTIONS:
        bands = sorted(band_heights(spans, h, GAP_MIN_PX, g), reverse=True) or [0.0]
        f[f"bands@{g}"] = len(bands)
        f[f"band1@{g}"] = bands[0] / h
        f[f"band2@{g}"] = (bands[1] if len(bands) > 1 else 0.0) / h
        f[f"band_med@{g}"] = float(np.median(bands)) / h

    # 行投影：峰的个数、空白行比例、自相关主峰（行距）
    profile = mask.sum(axis=1).astype(np.float64)
    k = max(1, h // 50)
    smooth = np.convolve(profile, np.ones(k) / k, mode="same")
    top = smooth.max()
    peaks, _ = signal.find_peaks(np.r_[0.0, smooth, 0.0], prominence=0.2 * top)
    f["profile_peaks"] = len(peaks)
    f["empty_rows"] = float(np.mean(smooth < 0.05 * top))
    centered = profile - profile.mean()
    ac = np.correlate(centered, centered, "full")[h - 1:]
    lo, hi = max(1, int(0.1 * h)), int(0.9 * h)
    if ac[0] > 0 and hi > lo:
        lag = lo + int(np.argmax(ac[lo:hi]))
        f["ac_lag"], f["ac_peak"] = lag / h, float(ac[lag] / ac[0])
    else:
        f["ac_lag"] = f["ac_peak"] = 0.0

    # 连通域（8 邻域），过滤掉噪点
    labels, _ = ndimage.label(mask, structure=np.ones((3, 3), dtype=bool))
    areas = np.bincount(labels.ravel())[1:]
    boxes = [b for b, a in zip(ndimage.find_objects(labels), areas) if a >= max(2, 0.002 * ink)]
    hs = np.array([b[0].stop - b[0].start for b in boxes], dtype=np.float64)
    ws = np.array([b[1].stop - b[1].start for b in boxes], dtype=np.float64)
    f["cc_count"] = math.log1p(len(boxes))
    for q in (10, 25, 50, 75, 90, 100):
        f[f"cc_h_q{q}"] = float(np.percentile(hs, q)) / h if len(hs) else 0.0
    f["cc_h_med_vs_rule"] = float(np.median(hs)) / rule if len(hs) else 0.0
    f["cc_w_med"] = float(np.median(ws)) / w if len(ws) else 0.0
    f["cc_w_max"] = float(ws.max()) / w if len(ws) else 0.0
    lines = [b for b, bh, bw in zip(boxes, hs, ws) if bw > 3 * bh and bw > 0.15 * w]
    f["thin_lines"] = len(lines)
    bars = 0                                                 # 上下都有连通域的横线，多半是分数线
    for line in lines:
        x0, x1, y = line[1].start, line[1].stop, (line[0].start + line[0].stop) / 2
        above = below = False
        for b in boxes:
            if b is line or b[1].stop <= x0 or b[1].start >= x1:
                continue
            centre = (b[0].start + b[0].stop) / 2
            above |= centre < y
            below |= centre > y
        bars += above and below
    f["fraction_bars"] = bars

    # 笔画粗细：横向游程长度中位数
    edges = np.diff(np.pad(mask, ((0, 0), (1, 1))).astype(np.int8), axis=1)
    runs = np.nonzero(edges == -1)[1] - np.nonzero(edges == 1)[1]
    f["stroke"] = float(np.median(runs)) / h

    # 倾斜：哪个剪切角让行投影最"尖"，以及剪切后的行带
    ys, xs = np.nonzero(mask)
    if len(ys) > MAX_SHEAR_PIXELS:
        pick = np.random.default_rng(0).choice(len(ys), MAX_SHEAR_PIXELS, replace=False)
        ys, xs = ys[pick], xs[pick]
    best = (-1.0, 0.0, None)
    sharp0 = 1.0
    for s in SHEARS:
        yy = np.round(ys + s * (xs - w / 2)).astype(np.int64)
        prof = np.bincount(yy - yy.min())
        sharp = float((prof.astype(np.float64) ** 2).sum()) / len(ys) ** 2
        if s == 0:
            sharp0 = sharp
        if sharp > best[0]:
            best = (sharp, float(s), prof)
    f["shear_best"] = best[1]
    f["shear_gain"] = best[0] / sharp0
    sheared = band_heights(row_spans(best[2] > 0), len(best[2]), GAP_MIN_PX)
    f["shear_bands"] = len(sheared)
    f["shear_band1"] = max(sheared) / h
    return f


def crop_to_ink(mask: np.ndarray) -> np.ndarray:
    rows, cols = np.nonzero(mask.any(axis=1))[0], np.nonzero(mask.any(axis=0))[0]
    if not len(rows):
        raise ValueError("没有笔迹")
    return mask[rows[0]:rows[-1] + 1, cols[0]:cols[-1] + 1]


def sample_mask(kind: str, path: str) -> np.ndarray:
    """运行时能拿到的二值笔迹图。InkML 先按原规则渲染成图片再取笔迹。"""
    if kind == "image":
        return formula_ink_mask(Path(path))
    return crop_to_ink(render(read_strokes(Path(path)))[0].numpy() > 0.5)


def sample_input(kind: str, path: str, band_scale: float) -> torch.Tensor:
    """按系数 m 预处理成识别模型的输入，和训练、运行时同一套代码。"""
    if kind == "image":
        return load_formula_image(Path(path), invert=IMAGE_INVERT, band_scale=band_scale)
    return render(read_strokes(Path(path)), band_scale=band_scale)


# ---------- 用识别模型打标签 ----------
def load_recognizer(path: Path, device: str) -> vital_mini:
    model = vital_mini()
    checkpoint = torch.load(path, map_location="cpu", weights_only=True)
    load_training_weights(model, checkpoint["model"] if "model" in checkpoint else checkpoint)
    return model.to(device).eval()


@torch.no_grad()
def score_batch(model: vital_mini, pairs: list[tuple[torch.Tensor, list[int]]], device: str):
    """(图, token id) 列表 -> 每条的平均 token NLL、teacher forcing 下猜对的 token 数、token 数。"""
    image, memory_mask, tokens_in, tgt_mask, target = [t.to(device) for t in collate(pairs)]
    with torch.autocast(device_type=device, dtype=torch.bfloat16, enabled=device == "cuda"):
        logits = model(image, tokens_in, tgt_key_padding_mask=tgt_mask, memory_key_padding_mask=memory_mask)
    logits = logits.float()
    keep = target != pad_id
    nll = F.cross_entropy(logits.transpose(1, 2), target, ignore_index=pad_id, reduction="none")
    count = keep.sum(1)
    correct = ((logits.argmax(-1) == target) & keep).sum(1)
    return (nll.sum(1) / count).cpu(), correct.cpu(), count.cpu()


class CandidateSet(torch.utils.data.Dataset):
    """一个样本 -> (特征, 各候选 m 下的输入图)。放在 DataLoader 子进程里并行做预处理。"""

    def __init__(self, items: list[tuple[str, str, list[int]]]):
        self.items = items

    def __len__(self) -> int:
        return len(self.items)

    def __getitem__(self, i: int):
        kind, path, _ = self.items[i]
        try:
            features = ink_features(sample_mask(kind, path))
            return i, features, [sample_input(kind, path, m) for m in CANDIDATES], None
        except Exception as error:                            # 坏样本跳过，不中断整轮
            return i, None, None, f"{type(error).__name__}: {error}"


def keep_as_is(sample):
    return sample


def pick_samples(per_source: int, seed: int) -> tuple[list, list[str]]:
    """混合数据的 valid（识别模型没训练过）每个来源抽 per_source 条，加上全部个人 val/ 截图。"""
    vocab = {t: i for i, t in enumerate(load_vocab())}
    dataset = MathWriting(str(HERE / "data" / "mixed"), ["valid"], vocab, train=False, image_roots=[str(HERE)])
    personal = str((HERE / "val").resolve())
    groups: dict[str, list] = {}
    for item, source in zip(dataset.items, dataset.sources):
        groups.setdefault("personal" if item[1].startswith(personal) else source, []).append(item)
    rng = random.Random(seed)
    items, sources = [], []
    for source in sorted(groups):
        chosen = groups[source] if source == "personal" else rng.sample(groups[source], min(per_source, len(groups[source])))
        items += chosen
        sources += [source] * len(chosen)
    return items, sources


def label(args):
    device = "cuda" if torch.cuda.is_available() else "cpu"
    items, sources = pick_samples(args.per_source, args.seed)
    print(f"样本: {dict(Counter(sources))} | 候选 m: {[round(m, 3) for m in CANDIDATES]} | {device}", flush=True)
    model = load_recognizer(args.weights, device)
    loader = torch.utils.data.DataLoader(CandidateSet(items), batch_size=None, num_workers=args.workers,
                                         collate_fn=keep_as_is)
    K = len(CANDIDATES)
    nll = np.full((len(items), K), np.nan, dtype=np.float32)
    correct = np.zeros((len(items), K), dtype=np.int32)
    tokens = np.zeros(len(items), dtype=np.int32)
    features: dict[int, dict] = {}
    failures = []
    pending: list[tuple[int, int, torch.Tensor]] = []        # (样本, 候选, 图)

    def flush():
        pending.sort(key=lambda p: (p[2].size(-2), p[2].size(-1)))
        start = 0
        while start < len(pending):
            end = start
            # 按补边后的特征格数控制每批大小，多行大图自动少放几张
            while end < len(pending) and (end - start + 1) * pending[end][2].size(-2) * pending[end][2].size(-1) \
                    <= args.batch_pixels and end - start < 128:
                end += 1
            end = max(end, start + 1)
            chunk = pending[start:end]
            mean_nll, hit, count = score_batch(model, [(im, items[i][2]) for i, _, im in chunk], device)
            for (i, k, _), a, b, c in zip(chunk, mean_nll.tolist(), hit.tolist(), count.tolist()):
                nll[i, k], correct[i, k], tokens[i] = a, b, c
            start = end
        pending.clear()

    started = time.monotonic()
    for n, (i, feats, images, error) in enumerate(loader, 1):
        if error is not None:
            failures.append((items[i][1], error))
            continue
        features[i] = feats
        pending += [(i, k, im) for k, im in enumerate(images)]
        if len(pending) >= args.flush * K:
            flush()
        if n % 500 == 0:
            print(f"  {n}/{len(items)} | {time.monotonic() - started:.0f}s", flush=True)
    flush()

    done = sorted(features)
    names = list(features[done[0]])
    OUT_DIR.mkdir(parents=True, exist_ok=True)
    np.savez_compressed(
        args.out, features=np.array([[features[i][k] for k in names] for i in done], dtype=np.float64),
        feature_names=np.array(names), nll=nll[done], correct=correct[done], tokens=tokens[done],
        sources=np.array([sources[i] for i in done]), kinds=np.array([items[i][0] for i in done]),
        paths=np.array([items[i][1] for i in done]), candidates=np.array(CANDIDATES))
    print(f"完成 {len(done)} 条，失败 {len(failures)} 条，用时 {time.monotonic() - started:.0f}s -> {args.out}")
    for path, error in failures[:5]:
        print(f"  失败: {path}: {error}")


# ---------- LightGBM ----------
def surface_rows(X: np.ndarray) -> np.ndarray:
    """每个样本展开成 K 行：特征 + log2(m)。"""
    return np.hstack([np.repeat(X, len(CANDIDATES), axis=0), np.tile(LOG2_CANDIDATES, len(X))[:, None]])


def soft_target(nll: np.ndarray) -> np.ndarray:
    """NLL 曲线上的软 argmin（log2 m），比硬 argmin 少受平坦曲线上噪声的影响。"""
    weight = np.exp(-(nll - nll.min(axis=1, keepdims=True)) / SOFT_TAU)
    return (weight * LOG2_CANDIDATES).sum(axis=1) / weight.sum(axis=1)


def fit_booster(method: str, X: np.ndarray, nll: np.ndarray):
    import lightgbm as lgb
    if method == "surface":   # 学"换成 m 后 NLL 比原规则变化多少"，预测时取最低的那个 m
        data = lgb.Dataset(surface_rows(X), (nll - nll[:, [RULE_INDEX]]).ravel())
    else:                     # direct：直接回归最佳 log2 m
        data = lgb.Dataset(X, soft_target(nll))
    return lgb.train(LGB_PARAMS, data, num_boost_round=LGB_ROUNDS)


def choose(method: str, booster, X: np.ndarray) -> np.ndarray:
    """预测 -> 每个样本选中的候选下标。"""
    if method == "surface":
        delta = booster.predict(surface_rows(X)).reshape(len(X), len(CANDIDATES))
        delta[:, RULE_INDEX] = 0.0                     # 预测不会更好时保持原规则
        return delta.argmin(axis=1)
    return np.abs(booster.predict(X)[:, None] - LOG2_CANDIDATES[None, :]).argmin(axis=1)


def out_of_fold(X: np.ndarray, nll: np.ndarray, folds: int, seed: int) -> dict[str, np.ndarray]:
    """K 折交叉验证：每个样本的选择都来自没见过它的模型。"""
    order = np.random.default_rng(seed).permutation(len(X))
    picks = {name: np.zeros(len(X), dtype=np.int64) for name in ("constant", "direct", "surface")}
    for fold in range(folds):
        test = order[fold::folds]
        train = np.setdiff1d(order, test)
        picks["constant"][test] = int(nll[train].mean(axis=0).argmin())
        for method in ("direct", "surface"):
            picks[method][test] = choose(method, fit_booster(method, X[train], nll[train]), X[test])
        print(f"  第 {fold + 1}/{folds} 折完成", flush=True)
    picks["rule"] = np.full(len(X), RULE_INDEX)
    picks["oracle"] = nll.argmin(axis=1)
    return picks


def summarize(pick: np.ndarray, table: dict, mask: np.ndarray) -> dict:
    rows = np.nonzero(mask)[0]
    chosen = pick[rows]
    nll = table["nll"][rows, chosen]
    rule = table["nll"][rows, RULE_INDEX]
    oracle = table["nll"][rows].min(axis=1)
    hit = table["correct"][rows, chosen]
    tokens = table["tokens"][rows]
    best = table["nll"][rows].argmin(axis=1)
    gap = (rule - oracle).mean()
    return {
        "n": int(len(rows)),
        "nll": float(nll.mean()),
        "token_acc": float(hit.sum() / tokens.sum()),
        "seq_acc": float((hit == tokens).mean()),        # teacher forcing 下整条都猜对
        "changed": float((chosen != RULE_INDEX).mean()),
        "gain_captured": float((rule - nll).mean() / gap) if gap > 0 else 0.0,
        "step_error": float(np.abs(chosen - best).mean()),   # 与最佳候选差几档（每档 2^0.25）
        "within_1_step": float((np.abs(chosen - best) <= 1).mean()),
    }


@torch.no_grad()
def greedy_check(table: dict, pick: np.ndarray, n: int, weights: Path, seed: int) -> dict:
    """在预测改变了缩放的样本里抽 n 条，贪心解码比较整式正确率（原规则 vs 预测）。"""
    changed = np.nonzero(pick != RULE_INDEX)[0]
    rows = np.random.default_rng(seed).permutation(changed)[:n]
    if not len(rows):
        return {"n": 0}
    device = "cuda" if torch.cuda.is_available() else "cpu"
    model = load_recognizer(weights, device)
    vocab = {t: i for i, t in enumerate(load_vocab())}
    from labels import tokenize_label
    labels_by_path = {}
    for manifest in (HERE / "data" / "mixed" / "valid.jsonl", HERE / "val" / "labels.jsonl"):
        with manifest.open(encoding="utf-8") as stream:
            for line in stream:
                row = json.loads(line)
                labels_by_path[str((manifest.parent / (row.get("image") or row["path"])).resolve())] = row["latex"]
    wins = {"rule": 0, "predicted": 0}
    for r in rows:
        kind, path = str(table["kinds"][r]), str(table["paths"][r])
        ids = [vocab[t] for t in tokenize_label(labels_by_path[path])]
        for name, k in (("rule", RULE_INDEX), ("predicted", int(pick[r]))):
            image = convert([sample_input(kind, path, CANDIDATES[k])])[0].to(device)
            wins[name] += int(model.generate(image, beam_width=1, max_steps=max_len)[0] == ids)
    return {"n": int(len(rows)), "changed_total": int(len(changed)),
            "rule_exact": wins["rule"] / len(rows), "predicted_exact": wins["predicted"] / len(rows)}


def load_table(path: Path) -> dict:
    with np.load(path) as data:
        table = {key: data[key] for key in data.files}
    if not np.allclose(table["candidates"], CANDIDATES):
        raise ValueError("标签表的候选 m 和当前 CANDIDATES 不一致，请重新运行 label")
    return table


def fit(args):
    table = load_table(args.table)
    X, nll, sources = table["features"], table["nll"], table["sources"]
    print(f"{len(X)} 条样本，{X.shape[1]} 个特征 | {dict(Counter(sources.tolist()))}", flush=True)
    picks = out_of_fold(X, nll, args.folds, args.seed)

    groups = ["全部"] + sorted(set(sources.tolist()))
    report = {"candidates": list(CANDIDATES), "methods": {}}
    for method, pick in picks.items():
        report["methods"][method] = {g: summarize(pick, table, np.ones(len(X), bool) if g == "全部" else sources == g)
                                     for g in groups}
    print_report(report, groups)

    method = min(("direct", "surface"), key=lambda m: report["methods"][m]["全部"]["nll"])
    print(f"\n交叉验证更好的方法: {method}", flush=True)
    if args.exp_n:
        report["greedy_check"] = greedy_check(table, picks[method], args.exp_n, args.weights, args.seed)
        print(f"贪心解码整式正确率（只在预测改变缩放的样本里抽查）: {report['greedy_check']}")

    booster = fit_booster(method, X, nll)
    booster.save_model(str(args.model), importance_type="gain")
    importance = sorted(zip(table["feature_names"].tolist() + (["log2_m"] if method == "surface" else []),
                            booster.feature_importance("gain").tolist()), key=lambda p: -p[1])
    report.update(method=method, feature_names=table["feature_names"].tolist(),
                  importance=[[name, float(gain)] for name, gain in importance[:15]])
    args.model.with_suffix(".json").write_text(json.dumps(report, ensure_ascii=False, indent=2), encoding="utf-8")
    print(f"模型 -> {args.model}\n报告 -> {args.model.with_suffix('.json')}")
    print("特征重要性前 10:", ", ".join(name for name, _ in importance[:10]))


def print_report(report: dict, groups: list[str]):
    columns = ("nll", "token_acc", "seq_acc", "changed", "gain_captured", "within_1_step")
    for g in groups:
        print(f"\n[{g}] n={report['methods']['rule'][g]['n']}")
        print(f"  {'方法':<9}" + "".join(f"{c:>15}" for c in columns))
        for method, by_group in report["methods"].items():
            print(f"  {method:<9}" + "".join(f"{by_group[g][c]:>15.4f}" for c in columns))


# ---------- 推理 ----------
class CropModel:
    """训练好的缩放模型：二值笔迹图 -> 系数 m（传给 load_formula_image 的 band_scale）。"""

    def __init__(self, path: Path = MODEL_PATH):
        import lightgbm as lgb
        meta = json.loads(path.with_suffix(".json").read_text(encoding="utf-8"))
        if not np.allclose(meta["candidates"], CANDIDATES):
            raise ValueError("模型的候选 m 和当前 CANDIDATES 不一致")
        self.method, self.feature_names = meta["method"], meta["feature_names"]
        self.booster = lgb.Booster(model_file=str(path))

    def band_scale(self, mask: np.ndarray) -> float:
        features = ink_features(mask)
        X = np.array([[features[name] for name in self.feature_names]], dtype=np.float64)
        return CANDIDATES[int(choose(self.method, self.booster, X)[0])]


def predict(args):
    crop = CropModel(args.model)
    for path in args.images:
        mask = formula_ink_mask(Path(path))
        m = crop.band_scale(mask)
        h, w = mask.shape
        rule = max(tallest_band(row_spans(mask.any(axis=1)), h, GAP_MIN_PX), FLAT_FRACTION * w)
        layout = formula_layout(w, h, rule * m)
        print(f"{path}: m={m:.3f} | 缩放 {layout.scale:.3f}（原规则 {formula_layout(w, h, rule).scale:.3f}）"
              f" | 画布 {layout.width}x{layout.height}")


def main(argv=None):
    ap = argparse.ArgumentParser(description="LightGBM 预测公式图片的最佳缩放系数")
    sub = ap.add_subparsers(dest="command", required=True)
    p = sub.add_parser("label", help="用识别模型给验证样本打标签")
    p.add_argument("--weights", type=Path, default=CHECKPOINTS / "model.pt")
    p.add_argument("--per-source", type=int, default=1500, help="每个来源抽多少条 valid 样本")
    p.add_argument("--workers", type=int, default=6)
    p.add_argument("--flush", type=int, default=64, help="攒够多少个样本送一次 GPU")
    p.add_argument("--batch-pixels", type=int, default=64 * 128 * 512, help="每批补边后的像素上限")
    p.add_argument("--seed", type=int, default=0)
    p.add_argument("--out", type=Path, default=TABLE_PATH)
    p.set_defaults(run=label)
    p = sub.add_parser("fit", help="交叉验证评估并训练最终模型")
    p.add_argument("--table", type=Path, default=TABLE_PATH)
    p.add_argument("--model", type=Path, default=MODEL_PATH)
    p.add_argument("--weights", type=Path, default=CHECKPOINTS / "model.pt", help="贪心解码抽查用的识别模型")
    p.add_argument("--folds", type=int, default=5)
    p.add_argument("--exp-n", type=int, default=300, help="贪心解码抽查条数，0=不抽查")
    p.add_argument("--seed", type=int, default=0)
    p.set_defaults(run=fit)
    p = sub.add_parser("predict", help="预测图片的缩放系数")
    p.add_argument("images", nargs="+")
    p.add_argument("--model", type=Path, default=MODEL_PATH)
    p.set_defaults(run=predict)
    args = ap.parse_args(argv)
    args.run(args)


if __name__ == "__main__":
    main()
