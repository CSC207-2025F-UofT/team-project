"""训练"公式图片最佳缩放系数"模型（LightGBM），导出给训练和 Java 运行时使用。

缩放系数 m 的含义、特征和运行时推理见 crop_scale.py；本文件负责打标签、评估、训练和导出。

"最佳"由识别模型自己决定：对每张没参与训练的验证图，依次试 CANDIDATES 里的每个 m，用 teacher
forcing 算标签序列的平均 token 负对数似然（NLL），NLL 越低说明模型在这个缩放下越容易读对。

用法（在 src/neutral network/ 下，用装了 torch、lightgbm、scipy 的 .venv）：
  python crop.py label              # 跑识别模型，生成每张图在各候选 m 下的 NLL 表
  python crop.py fit                # 交叉验证评估各方法，训练最终模型并导出 crop_scale.json
  python crop.py cache              # 给训练数据预先算好 m，train.py 直接读缓存
  python crop.py predict a.png ...  # 预测图片的 m

导出的 crop_scale.json 默认在 checkpoints/crop/，train.py 默认读取它；Java 运行时读取
src/main/java/model/checkpoints/crop_scale.json，换模型时要把同一份文件复制过去。
注意：缩放偏好是从当前识别模型学来的，识别模型重新训练后应重新 label + fit。
"""
import argparse
import json
import random
import time
from collections import Counter
from pathlib import Path

import numpy as np
import torch
import torch.nn.functional as F

from crop_scale import (CANDIDATES, DEFAULT_PATH, LOG2_CANDIDATES, RULE_INDEX, CropScale, ink_features,
                        save_model, trees_from_lightgbm)
from formula_layout import FLAT_FRACTION, GAP_MIN_PX, formula_layout, row_spans, tallest_band
from model import load_training_weights, load_vocab, max_len, pad_id, vital_mini
from preprocess import formula_ink_mask, load_formula_image
from train import (IMAGE_INVERT, MathWriting, collate, convert, formula_mask, read_strokes, render,
                   save_scale_cache, scale_cache_path, load_scale_cache)

HERE = Path(__file__).resolve().parent
CHECKPOINTS = HERE.parents[1] / "checkpoints"
OUT_DIR = CHECKPOINTS / "crop"
TABLE_PATH = OUT_DIR / "labels.npz"
SOFT_TAU = 0.02                                              # direct 方法的软 argmin 温度（NLL 单位）
LGB_PARAMS = dict(objective="regression", learning_rate=0.05, num_leaves=31, min_data_in_leaf=40,
                  feature_fraction=0.8, bagging_fraction=0.8, bagging_freq=1, lambda_l2=1.0,
                  verbose=-1, seed=0, num_threads=0)
LGB_ROUNDS = 300


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
            features = ink_features(formula_mask(kind, path))
            return i, features, [sample_input(kind, path, m) for m in CANDIDATES], None
        except Exception as error:                            # 坏样本跳过，不中断整轮
            return i, None, None, f"{type(error).__name__}: {error}"


def keep_as_is(sample):
    return sample


def vocab_ids() -> dict[str, int]:
    return {t: i for i, t in enumerate(load_vocab())}


def pick_samples(per_source: int, seed: int) -> tuple[list, list[str]]:
    """混合数据的 valid（识别模型没训练过）每个来源抽 per_source 条，加上全部个人 val/ 截图。"""
    dataset = MathWriting(str(HERE / "data" / "mixed"), ["valid"], vocab_ids(), train=False, image_roots=[str(HERE)])
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
            # 按补边后的像素数控制每批大小，多行大图自动少放几张
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
    args.out.parent.mkdir(parents=True, exist_ok=True)
    np.savez_compressed(
        args.out, features=np.array([[features[i][k] for k in names] for i in done], dtype=np.float64),
        feature_names=np.array(names), nll=nll[done], correct=correct[done], tokens=tokens[done],
        sources=np.array([sources[i] for i in done]), kinds=np.array([items[i][0] for i in done]),
        paths=np.array([items[i][1] for i in done]), candidates=np.array(CANDIDATES),
        weights=np.array(str(Path(args.weights).resolve())))
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
    """预测 -> 每个样本选中的候选下标。与 CropScale.choose_index 的规则相同。"""
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
    vocab = vocab_ids()
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
    if table["feature_names"].tolist() != list(ink_features(np.ones((4, 4), dtype=bool))):
        raise ValueError("标签表的特征和当前 crop_scale.ink_features 不一致，请重新运行 label")
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

    method = args.method or min(("direct", "surface"), key=lambda m: report["methods"][m]["全部"]["nll"])
    print(f"\n导出方法: {method}", flush=True)
    if args.exp_n:
        report["greedy_check"] = greedy_check(table, picks[method], args.exp_n, args.weights, args.seed)
        print(f"贪心解码整式正确率（只在预测改变缩放的样本里抽查）: {report['greedy_check']}")

    booster = fit_booster(method, X, nll)
    names = table["feature_names"].tolist()
    save_model(args.model, method, names, trees_from_lightgbm(booster),
               {"recognizer": str(table.get("weights", "")), "samples": int(len(X))})
    exported = CropScale(args.model)
    lgb_pick = choose(method, booster, X)
    json_pick = np.array([exported.choose_index(dict(zip(names, row))) for row in X.tolist()])
    if not (lgb_pick == json_pick).all():                     # 导出的树必须和 LightGBM 逐条一致
        raise RuntimeError(f"导出的树与 LightGBM 预测不一致: {(lgb_pick != json_pick).sum()} 条")
    importance = sorted(zip(names + (["log2_m"] if method == "surface" else []),
                            booster.feature_importance("gain").tolist()), key=lambda p: -p[1])
    report.update(method=method, model_sha256=exported.sha256,
                  importance=[[name, float(gain)] for name, gain in importance[:15]])
    report_path = args.model.with_name(args.model.stem + "_report.json")
    report_path.write_text(json.dumps(report, ensure_ascii=False, indent=2), encoding="utf-8")
    print(f"模型 -> {args.model}（sha {exported.sha256[:12]}，与 LightGBM 逐条一致）\n报告 -> {report_path}")
    print("特征重要性前 10:", ", ".join(name for name, _ in importance[:10]))


def print_report(report: dict, groups: list[str]):
    columns = ("nll", "token_acc", "seq_acc", "changed", "gain_captured", "within_1_step")
    for g in groups:
        print(f"\n[{g}] n={report['methods']['rule'][g]['n']}")
        print(f"  {'方法':<9}" + "".join(f"{c:>15}" for c in columns))
        for method, by_group in report["methods"].items():
            print(f"  {method:<9}" + "".join(f"{by_group[g][c]:>15.4f}" for c in columns))


# ---------- 给训练数据预先算 m ----------
class ScaleSet(torch.utils.data.Dataset):
    def __init__(self, items: list[tuple[str, str]], model_path: Path):
        self.items, self.model_path, self.model = items, model_path, None

    def __len__(self) -> int:
        return len(self.items)

    def __getitem__(self, i: int):
        if self.model is None:                                # 每个子进程各读一次模型
            self.model = CropScale(self.model_path)
        kind, path = self.items[i]
        try:
            return path, self.model.band_scale(formula_mask(kind, path))
        except Exception:
            return path, None


def cache(args):
    """train.py 用到的全部样本（混合数据 + 个人数据）-> data/mixed/crop_scale_cache_<sha>.npz。"""
    model = CropScale(args.model)
    data_root = HERE / "data" / "mixed"
    path = scale_cache_path(data_root, model)
    scales = load_scale_cache(path)
    vocab = vocab_ids()
    items = {}
    for splits, extra in ((args.splits.split(","), {"personal_train": HERE / "train"}), (["valid"], {})):
        dataset = MathWriting(str(data_root), splits, vocab, train=bool(extra), **extra)
        items.update((p, (kind, p)) for kind, p, _ in dataset.items if p not in scales)
    todo = list(items.values())
    print(f"缩放模型 sha {model.sha256[:12]} | 已缓存 {len(scales)} 条，待计算 {len(todo)} 条 -> {path}", flush=True)
    loader = torch.utils.data.DataLoader(ScaleSet(todo, args.model), batch_size=None,
                                         num_workers=args.workers, collate_fn=keep_as_is)
    started, failed = time.monotonic(), 0
    for n, (p, m) in enumerate(loader, 1):
        if m is None:
            failed += 1
        else:
            scales[p] = m
        if n % args.save_every == 0 or n == len(todo):
            save_scale_cache(path, scales)
            print(f"  {n}/{len(todo)} | {time.monotonic() - started:.0f}s | 已保存", flush=True)
    if not todo:
        save_scale_cache(path, scales)
    counts = Counter(scales.values())
    print(f"完成，失败 {failed} 条。m 分布: {{{', '.join(f'{m:.2f}: {counts[m]}' for m in CANDIDATES)}}}")


def predict(args):
    model = CropScale(args.model)
    for path in args.images:
        mask = formula_ink_mask(Path(path))
        m = model.band_scale(mask)
        h, w = mask.shape
        rule = max(tallest_band(row_spans(mask.any(axis=1)), h, GAP_MIN_PX), FLAT_FRACTION * w)
        layout = formula_layout(w, h, rule * m)
        print(f"{path}: m={m:.3f} | 缩放 {layout.scale:.3f}（原规则 {formula_layout(w, h, rule).scale:.3f}）"
              f" | 画布 {layout.width}x{layout.height}")


def main(argv=None):
    ap = argparse.ArgumentParser(description="训练公式图片的缩放系数模型")
    sub = ap.add_subparsers(dest="command", required=True)
    p = sub.add_parser("label", help="用识别模型给验证样本打标签")
    p.add_argument("--weights", type=Path, default=CHECKPOINTS / "model.pt")
    p.add_argument("--per-source", type=int, default=2000, help="每个来源抽多少条 valid 样本")
    p.add_argument("--workers", type=int, default=6)
    p.add_argument("--flush", type=int, default=64, help="攒够多少个样本送一次 GPU")
    p.add_argument("--batch-pixels", type=int, default=64 * 128 * 512, help="每批补边后的像素上限")
    p.add_argument("--seed", type=int, default=0)
    p.add_argument("--out", type=Path, default=TABLE_PATH)
    p.set_defaults(run=label)
    p = sub.add_parser("fit", help="交叉验证评估，训练最终模型并导出 crop_scale.json")
    p.add_argument("--table", type=Path, default=TABLE_PATH)
    p.add_argument("--model", type=Path, default=DEFAULT_PATH)
    p.add_argument("--method", choices=["direct", "surface"], help="默认取交叉验证损失更低的")
    p.add_argument("--weights", type=Path, default=CHECKPOINTS / "model.pt", help="贪心解码抽查用的识别模型")
    p.add_argument("--folds", type=int, default=5)
    p.add_argument("--exp-n", type=int, default=300, help="贪心解码抽查条数，0=不抽查")
    p.add_argument("--seed", type=int, default=0)
    p.set_defaults(run=fit)
    p = sub.add_parser("cache", help="给训练数据预先算好缩放系数")
    p.add_argument("--model", type=Path, default=DEFAULT_PATH)
    p.add_argument("--splits", default="train", help="训练用的子集，与 train.py --splits 相同")
    p.add_argument("--workers", type=int, default=6)
    p.add_argument("--save-every", type=int, default=50000)
    p.set_defaults(run=cache)
    p = sub.add_parser("predict", help="预测图片的缩放系数")
    p.add_argument("images", nargs="+")
    p.add_argument("--model", type=Path, default=DEFAULT_PATH)
    p.set_defaults(run=predict)
    args = ap.parse_args(argv)
    args.run(args)


if __name__ == "__main__":
    main()
