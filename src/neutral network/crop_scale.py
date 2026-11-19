"""缩放系数模型的运行部分：二值笔迹图 -> 特征 -> 树模型 -> 系数 m。

m 乘在 formula_layout 的最高行带高度上（load_formula_image / render 的 band_scale）：
实际缩放 = 112 / (最高行带 × m)。m < 1 字放大，m > 1 字缩小，m = 1 即原规则。

训练（train.py）、Python 推理和 Java 的 FormulaImagePreprocessor 共用这一套：
- 特征只用整数计数和固定顺序的 double 运算，不用随机抽样、插值库或 scipy 的特殊约定，
  Java 端 CropScale.java 逐项照抄；改这里的任何特征都要同步改 Java，并重新生成对照数据。
- 树模型由 crop.py fit 从 LightGBM 导出成 JSON（crop_scale.json），Python 和 Java 都用自己的
  几十行遍历器读它，运行时不依赖 lightgbm。
本模块只依赖 numpy 和 formula_layout，不依赖 torch。
"""
import hashlib
import json
import math
from pathlib import Path

import numpy as np

from formula_layout import FLAT_FRACTION, GAP_MIN_PX, band_heights, row_spans, tallest_band

FORMAT = "crop-scale-trees-v1"
DEFAULT_PATH = Path(__file__).resolve().parents[2] / "checkpoints" / "crop" / "crop_scale.json"
CANDIDATES = tuple(2.0 ** (k / 4) for k in range(-6, 9))   # m: 0.35 ... 4，按 2^(1/4) 递增
LOG2_CANDIDATES = np.log2(CANDIDATES)
RULE_INDEX = CANDIDATES.index(1.0)
GAP_FRACTIONS = (0.005, 0.01, 0.02, 0.03, 0.05, 0.08)       # 多阈值行带
SHEAR_STEPS = tuple(range(-4, 5))                            # 剪切系数 = 步数 × SHEAR_UNIT
SHEAR_UNIT = 0.05
MAX_SHEAR_PIXELS = 200_000                                   # 超过就按固定步长取笔迹像素


# ---------- 与 Java 逐项一致的小工具 ----------
def percentile(values, q: float) -> float:
    """线性插值分位数：排序后取 (n-1)*q/100 处，a[lo] + (a[hi]-a[lo])*frac。空数组返回 0。"""
    a = sorted(values)
    if not a:
        return 0.0
    position = (len(a) - 1) * q / 100.0
    lo = int(math.floor(position))
    hi = min(lo + 1, len(a) - 1)
    return float(a[lo]) + (float(a[hi]) - float(a[lo])) * (position - lo)


def moving_average(values: np.ndarray, k: int) -> np.ndarray:
    """整数序列的滑动平均：位置 i 取 [i - k//2, i - k//2 + k) 的和（越界当 0）再除以 k。"""
    n = len(values)
    prefix = np.concatenate(([0], np.cumsum(values, dtype=np.int64)))
    index = np.arange(n)
    lo = np.clip(index - k // 2, 0, n)
    hi = np.clip(index - k // 2 + k, 0, n)
    return (prefix[hi] - prefix[lo]) / k


def count_peaks(values: np.ndarray, min_prominence: float) -> int:
    """两端补 0 后数峰：v[i] > v[i-1] 且 v[i] >= v[i+1]（平顶只算一次）。
    显著度 = 峰高 - max(左侧最低点, 右侧最低点)，两侧都只走到第一个比峰更高的点或端点；
    显著度 >= min_prominence 才计数。"""
    v = np.concatenate(([0.0], np.asarray(values, dtype=np.float64), [0.0]))
    count = 0
    for i in (np.nonzero((v[1:-1] > v[:-2]) & (v[1:-1] >= v[2:]))[0] + 1).tolist():
        left = v[:i][::-1]
        higher = np.nonzero(left > v[i])[0]
        left_min = left[:higher[0]].min() if len(higher) else left.min()
        right = v[i + 1:]
        higher = np.nonzero(right > v[i])[0]
        right_min = right[:higher[0]].min() if len(higher) else right.min()
        if v[i] - max(left_min, right_min) >= min_prominence:
            count += 1
    return count


def components(mask: np.ndarray) -> list[tuple[int, int, int, int, int]]:
    """8 邻域连通域 -> (上, 左, 下, 右, 面积)，下、右不含。
    连通域的集合是唯一确定的，Java 用广度优先搜索得到同样的集合；特征只用集合统计，不依赖顺序。"""
    from scipy import ndimage
    labels, _ = ndimage.label(mask, structure=np.ones((3, 3), dtype=bool))
    areas = np.bincount(labels.ravel())[1:].tolist()
    return [(b[0].start, b[1].start, b[0].stop, b[1].stop, a)
            for b, a in zip(ndimage.find_objects(labels), areas)]


# ---------- 特征 ----------
def ink_features(mask: np.ndarray) -> dict[str, float]:
    """裁剪后的二值笔迹图 [H,W] -> 特征（有序字典）。长度都除以笔迹高或宽，和分辨率无关。"""
    mask = np.asarray(mask, dtype=bool)
    h, w = mask.shape
    ink = max(int(mask.sum()), 1)
    spans = row_spans(mask.any(axis=1).tolist())
    rule = max(tallest_band(spans, h, GAP_MIN_PX), FLAT_FRACTION * w)
    f = {"log_aspect": math.log(w / h), "density": ink / (h * w), "rule_band": rule / h}

    for g in GAP_FRACTIONS:
        bands = sorted(band_heights(spans, h, GAP_MIN_PX, g), reverse=True) or [0.0]
        f[f"bands@{g}"] = float(len(bands))
        f[f"band1@{g}"] = bands[0] / h
        f[f"band2@{g}"] = (bands[1] if len(bands) > 1 else 0.0) / h
        f[f"band_med@{g}"] = percentile(bands, 50) / h

    # 行投影：峰的个数、空白行比例、自相关主峰（行距）
    profile = mask.sum(axis=1).astype(np.int64)
    smooth = moving_average(profile, max(1, h // 50))
    top = float(smooth.max())
    f["profile_peaks"] = float(count_peaks(smooth, 0.2 * top))
    f["empty_rows"] = float(np.mean(smooth < 0.05 * top))
    f["ac_lag"], f["ac_peak"] = autocorrelation_peak(profile)

    # 连通域（8 邻域），过滤掉噪点
    boxes = [b for b in components(mask) if b[4] >= max(2, 0.002 * ink)]
    hs = [b[2] - b[0] for b in boxes]
    ws = [b[3] - b[1] for b in boxes]
    f["cc_count"] = math.log1p(len(boxes))
    for q in (10, 25, 50, 75, 90, 100):
        f[f"cc_h_q{q}"] = percentile(hs, q) / h
    f["cc_h_med_vs_rule"] = percentile(hs, 50) / rule
    f["cc_w_med"] = percentile(ws, 50) / w
    f["cc_w_max"] = (max(ws) if ws else 0) / w
    lines = [b for b, bh, bw in zip(boxes, hs, ws) if bw > 3 * bh and bw > 0.15 * w]
    f["thin_lines"] = float(len(lines))
    bars = 0                                                 # 上下都有连通域的横线，多半是分数线
    for line in lines:
        middle2 = line[0] + line[2]                          # 中线纵坐标的 2 倍，避免小数
        above = below = False
        for b in boxes:
            if b is line or b[3] <= line[1] or b[1] >= line[3]:
                continue
            above |= b[0] + b[2] < middle2
            below |= b[0] + b[2] > middle2
        bars += above and below
    f["fraction_bars"] = float(bars)

    # 笔画粗细：横向游程长度中位数
    edges = np.diff(np.pad(mask, ((0, 0), (1, 1))).astype(np.int8), axis=1)
    runs = (np.nonzero(edges == -1)[1] - np.nonzero(edges == 1)[1]).tolist()
    f["stroke"] = percentile(runs, 50) / h

    f.update(shear_features(mask))
    return f


def autocorrelation_peak(profile: np.ndarray) -> tuple[float, float]:
    """去均值的行投影自相关，在滞后 [0.1h, 0.9h) 内找最高点 -> (滞后/h, 相关/零滞后相关)。
    sum((p_i-μ)(p_{i+L}-μ)) 展开成整数和再按固定顺序做 double 运算，Java 照同一式子算。"""
    n = len(profile)
    lo, hi = max(1, int(0.1 * n)), int(0.9 * n)
    if hi <= lo:
        return 0.0, 0.0
    p = profile.astype(np.int64)
    prefix = np.concatenate(([0], np.cumsum(p)))
    mu = int(prefix[-1]) / n
    lags = np.arange(hi)
    products = np.correlate(p, p, "full")[n - 1:n - 1 + hi]          # sum p_i p_{i+L}，整数精确
    heads_tails = prefix[n - lags] + (prefix[n] - prefix[lags])
    # 逐元素按 products - mu*(heads+tails) + (n-L)*mu*mu 的顺序做 double 运算
    ac = products.astype(np.float64) - mu * heads_tails.astype(np.float64) \
        + (n - lags).astype(np.float64) * mu * mu
    if ac[0] <= 0:
        return 0.0, 0.0
    best_lag = lo + int(np.argmax(ac[lo:hi]))                        # 并列取最小滞后
    return best_lag / n, float(ac[best_lag] / ac[0])


def shear_features(mask: np.ndarray) -> dict[str, float]:
    """试几个剪切系数，看哪个让行投影最"尖"（平方和最大），以及剪切后的行带。"""
    h, w = mask.shape
    ys, xs = np.nonzero(mask)                                # 行优先顺序，Java 同样逐行扫描
    if len(ys) > MAX_SHEAR_PIXELS:
        step = -(-len(ys) // MAX_SHEAR_PIXELS)
        ys, xs = ys[::step], xs[::step]
    n = len(ys)
    offset = xs - w / 2.0
    best_sharp, best_step, best_profile, sharp0 = -1.0, 0, None, 1.0
    for k in SHEAR_STEPS:
        yy = np.floor(ys + (k * SHEAR_UNIT) * offset + 0.5).astype(np.int64)
        prof = np.bincount(yy - yy.min())
        sharp = float(int((prof * prof).sum())) / float(n * n)
        if k == 0:
            sharp0 = sharp
        if sharp > best_sharp:
            best_sharp, best_step, best_profile = sharp, k, prof
    sheared = band_heights(row_spans((best_profile > 0).tolist()), len(best_profile), GAP_MIN_PX)
    return {"shear_best": best_step * SHEAR_UNIT, "shear_gain": best_sharp / sharp0,
            "shear_bands": float(len(sheared)), "shear_band1": max(sheared) / h}


def crop_to_ink(mask: np.ndarray) -> np.ndarray:
    rows, cols = np.nonzero(mask.any(axis=1))[0], np.nonzero(mask.any(axis=0))[0]
    if not len(rows):
        raise ValueError("没有笔迹")
    return mask[rows[0]:rows[-1] + 1, cols[0]:cols[-1] + 1]


# ---------- 树模型 ----------
def trees_from_lightgbm(booster) -> list[dict]:
    """LightGBM Booster -> 扁平数组表示的树：内部节点 split/threshold/left/right，
    子节点 >= 0 是内部节点下标，< 0 是叶子 ~index。判断规则 value <= threshold 走左边。"""
    trees = []
    for info in booster.dump_model()["tree_info"]:
        tree = {"split": [], "threshold": [], "left": [], "right": [], "leaf": []}

        def visit(node) -> int:
            if "leaf_value" in node:
                tree["leaf"].append(float(node["leaf_value"]))
                return ~(len(tree["leaf"]) - 1)
            if node["decision_type"] != "<=":
                raise ValueError(f"不支持的分裂类型: {node['decision_type']}")
            index = len(tree["split"])
            tree["split"].append(int(node["split_feature"]))
            tree["threshold"].append(float(node["threshold"]))
            tree["left"].append(0)
            tree["right"].append(0)
            tree["left"][index] = visit(node["left_child"])
            tree["right"][index] = visit(node["right_child"])
            return index

        visit(info["tree_structure"])
        trees.append(tree)
    return trees


def save_model(path: Path, method: str, feature_names: list[str], trees: list[dict], extra: dict) -> None:
    # 候选值和它们的 log2 直接写进文件，Java 读来用，避免两边 pow/log 的末位差异
    payload = {"format": FORMAT, "method": method, "candidates": list(CANDIDATES),
               "log2_candidates": LOG2_CANDIDATES.tolist(), "rule_index": RULE_INDEX,
               "feature_names": feature_names, "trees": trees, **extra}
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(payload, ensure_ascii=False), encoding="utf-8")


class CropScale:
    """读取 crop_scale.json：二值笔迹图 -> 系数 m。"""

    def __init__(self, path: Path = DEFAULT_PATH):
        raw = Path(path).read_bytes()
        self.sha256 = hashlib.sha256(raw).hexdigest()
        data = json.loads(raw)
        if data.get("format") != FORMAT:
            raise ValueError(f"不认识的缩放模型格式: {data.get('format')}")
        if not np.allclose(data["candidates"], CANDIDATES) or data["rule_index"] != RULE_INDEX:
            raise ValueError("缩放模型的候选 m 和当前 CANDIDATES 不一致")
        self.path, self.method, self.feature_names = Path(path), data["method"], data["feature_names"]
        self.trees = data["trees"]

    def raw_predict(self, row) -> float:
        total = 0.0
        for tree in self.trees:
            node = 0
            if tree["split"]:
                while node >= 0:
                    value = row[tree["split"][node]]
                    node = tree["left"][node] if value <= tree["threshold"][node] else tree["right"][node]
                total += tree["leaf"][~node]
            else:
                total += tree["leaf"][0]
        return total

    def choose_index(self, features: dict[str, float]) -> int:
        row = [features[name] for name in self.feature_names]
        if self.method == "surface":   # 每个候选预测"比原规则的 NLL 变化"，取最低；都不更好就保持原规则
            best, best_delta = RULE_INDEX, 0.0
            for k, log2_m in enumerate(LOG2_CANDIDATES.tolist()):
                delta = self.raw_predict(row + [log2_m])
                if k != RULE_INDEX and delta < best_delta:
                    best, best_delta = k, delta
            return best
        predicted = self.raw_predict(row)
        return min(range(len(CANDIDATES)), key=lambda k: (abs(predicted - LOG2_CANDIDATES[k]), k))

    def band_scale(self, mask: np.ndarray) -> float:
        return CANDIDATES[self.choose_index(ink_features(mask))]
