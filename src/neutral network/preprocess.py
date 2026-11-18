"""训练和识别共用的公式图片预处理。"""
from pathlib import Path

import numpy as np
import torch
from PIL import Image, ImageOps

from formula_layout import GAP_MIN_PX, formula_layout, round_half_up, row_spans, tallest_band


class EmptyFormulaImageError(ValueError):
    """图片中没有达到笔迹检测阈值的像素。"""


def _read_formula_image(path: Path) -> tuple[Image.Image, bool, tuple[int, int, int, int], np.ndarray]:
    """保留 RGB；根据任一颜色通道与黑/白背景的差异定位笔迹。
    最后一项是裁剪到笔迹包围盒的二值笔迹图 [H,W]，用来切行带。"""
    with Image.open(path) as source:
        rgba = ImageOps.exif_transpose(source).convert("RGBA")
        background = Image.new("RGBA", rgba.size, "white")
        rgb = Image.alpha_composite(background, rgba).convert("RGB")

    pixels = np.asarray(rgb)
    border = np.concatenate((pixels[0], pixels[-1], pixels[:, 0], pixels[:, -1]))
    light_background = bool(np.median(border) >= 128)
    foreground = 255 - pixels if light_background else pixels
    ink_mask = (foreground > 32).any(axis=2)
    bounds = Image.fromarray(ink_mask).getbbox()
    if bounds is None:
        raise EmptyFormulaImageError(f"图片中没有可识别的笔迹: {path}")
    return rgb, light_background, bounds, ink_mask[bounds[1]:bounds[3], bounds[0]:bounds[2]]


def check_formula_image(path: Path) -> None:
    """复用加载时的 RGB 笔迹检查，不缩放、不生成张量。"""
    _read_formula_image(path)


def formula_ink_mask(path: Path) -> np.ndarray:
    """与加载时相同的笔迹检测 -> 裁剪到包围盒的二值图 [H,W]（True 为笔迹）。"""
    return _read_formula_image(path)[3]


def load_formula_image(path: Path, invert: bool | None = None, band_scale: float = 1.0) -> torch.Tensor:
    """单张公式图 -> RGB [3, H, W]；H 至少 128，多行公式更高，H、W 都是 16 的倍数。
    缩放和画布尺寸见 formula_layout.py；invert=None 自动判断，否则强制开/关反色。
    band_scale 乘在规则算出的最高行带高度上（crop.py 用它试不同缩放），默认 1 即原规则。"""
    rgb, light_background, bounds, mask = _read_formula_image(path)
    should_invert = light_background if invert is None else invert
    # 裁切始终根据原图背景定位笔迹，不受输出是否反色影响。
    ink = (ImageOps.invert(rgb) if should_invert else rgb).crop(bounds)

    width, ink_height = ink.size
    tallest = tallest_band(row_spans(mask.any(axis=1)), ink_height, GAP_MIN_PX) * band_scale
    scale, canvas_width, canvas_height = formula_layout(width, ink_height, tallest)
    resized_width = max(1, round_half_up(width * scale))
    resized_height = max(1, round_half_up(ink_height * scale))
    ink = ink.resize((resized_width, resized_height), Image.Resampling.LANCZOS)
    background_value = 255 if light_background != should_invert else 0
    canvas = Image.new("RGB", (canvas_width, canvas_height), (background_value,) * 3)
    canvas.paste(ink, ((canvas_width - resized_width) // 2, (canvas_height - resized_height) // 2))
    tensor = torch.from_numpy(np.asarray(canvas, dtype=np.float32) / 255.0)
    return tensor.permute(2, 0, 1).contiguous()

