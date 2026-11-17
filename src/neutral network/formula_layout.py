"""公式图片的版面规则：缩放比例和画布尺寸。

训练（preprocess.py 的图片路径、train.py 的笔迹渲染）和运行（Java 的
FormulaImagePreprocessor）共用这一套规则，Java 端的常量和算法必须与此保持一致。
本模块只用标准库，方便单独测试。

规则：
1. 把笔迹按竖直方向切成"行带"：相邻两段笔迹之间的空白达到阈值才算两个带。
2. 缩放比例让最高的行带高 LINE_PX（加上两侧 MARGIN 正好是 128）。单行公式只有一个带，
   所以和旧规则一样；多行公式每行大小不变，画布高度随行数增长。
   分式、上下标等被切成几个带时，最高带仍是正常字高，字符大小保持稳定。
3. 画布高度、宽度向上取整到 STRIDE 的倍数，高度至少 MIN_H。
4. 只有画布高度超过 MAX_H 或特征格数超过 MAX_TOKENS 时，才整体等比缩小。
"""
import math
from typing import Iterable, NamedTuple

LINE_PX = 112          # 缩放后最高行带的高度
MARGIN = 8             # 笔迹四周留白
STRIDE = 16            # vital_pixel 的总下采样倍数，画布宽高都是它的倍数
MIN_W = 64
MIN_H = 128
MAX_H = 512
MAX_TOKENS = 1280      # (高/STRIDE) * (宽/STRIDE)，即编码器的特征格数
GAP_FRACTION = 0.03    # 行带之间的最小空白，占笔迹总高的比例
GAP_MIN_PX = 2         # 像素图片上的最小空白（笔画坐标没有这个下限）
FLAT_FRACTION = 0.05   # 只有一条极扁的线时，最高带至少取笔迹宽度的这个比例，避免被放大到离谱
SHRINK_STEP = 0.98     # 超出上限时每次缩小的比例


class FormulaLayout(NamedTuple):
    scale: float
    width: int
    height: int


def round_half_up(value: float) -> int:
    """四舍五入，.5 向上；Java 的 Math.round 与此相同，Python 内置 round 不是。"""
    return int(math.floor(value + 0.5))


def snap_up(value: float) -> int:
    """向上取整到 STRIDE 的倍数；容差避免浮点误差把恰好整除的值多进一格。"""
    return int(math.ceil(value / STRIDE - 1e-9)) * STRIDE


def row_spans(occupied: Iterable[bool]) -> list[tuple[int, int]]:
    """逐行是否有笔迹 -> 连续有笔迹的 (起始行, 结束行)，结束行不含。"""
    spans = []
    start = None
    count = 0
    for y, filled in enumerate(occupied):
        count = y + 1
        if filled and start is None:
            start = y
        elif not filled and start is not None:
            spans.append((start, y))
            start = None
    if start is not None:
        spans.append((start, count))
    return spans


def tallest_band(spans: Iterable[tuple[float, float]], ink_height: float, min_gap: float = 0.0) -> float:
    """把竖直范围合并成行带，返回最高带的高度。

    spans 是 (上, 下) 范围，下不含；相邻范围之间的空白小于 max(GAP_FRACTION*ink_height, min_gap)
    就并成同一个带。像素图片传 min_gap=GAP_MIN_PX，笔画坐标不传。
    """
    gap = max(GAP_FRACTION * ink_height, min_gap)
    best = 0.0
    start = end = None
    for top, bottom in sorted(spans):
        if start is None:
            start, end = top, bottom
        elif top - end < gap:
            end = max(end, bottom)
        else:
            best = max(best, end - start)
            start, end = top, bottom
    if start is not None:
        best = max(best, end - start)
    return best


def formula_layout(ink_width: float, ink_height: float, tallest: float) -> FormulaLayout:
    """笔迹宽高（含笔画粗细）和最高行带 -> 缩放比例与画布宽高。"""
    base = max(tallest, FLAT_FRACTION * ink_width)
    scale = LINE_PX / base
    while True:
        width = max(MIN_W, snap_up(ink_width * scale + 2 * MARGIN))
        height = max(MIN_H, snap_up(ink_height * scale + 2 * MARGIN))
        if height <= MAX_H and (height // STRIDE) * (width // STRIDE) <= MAX_TOKENS:
            return FormulaLayout(scale, width, height)
        scale *= SHRINK_STEP
