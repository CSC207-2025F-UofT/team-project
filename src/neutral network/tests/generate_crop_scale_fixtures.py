"""生成缩放模型的 Python/Java 对照数据：src/test/java/data_access/crop_scale_fixtures.json。

python tests/generate_crop_scale_fixtures.py [--model 路径]
每个用例是一张二值笔迹图（逐行游程编码）、Python 算出的特征和模型选中的候选下标。
Java 的 CropScaleTest 和 Python 的 test_crop.py 都读这份文件；改了特征或换了模型就重新生成。
"""
import argparse
import json
import sys
from pathlib import Path

import numpy as np

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
from crop_scale import CropScale, ink_features

ROOT = Path(__file__).resolve().parents[3]
OUTPUT = ROOT / "src" / "test" / "java" / "data_access" / "crop_scale_fixtures.json"
JAVA_MODEL = ROOT / "src" / "main" / "java" / "model" / "checkpoints" / "crop_scale.json"


def encode(mask: np.ndarray) -> list[int]:
    """逐行优先展平后的游程长度，从 False 开始交替。"""
    runs, value, length = [], False, 0
    for pixel in mask.ravel().tolist():
        if pixel == value:
            length += 1
        else:
            runs.append(length)
            value, length = pixel, 1
    runs.append(length)
    return runs


def decode(runs: list[int], height: int, width: int) -> np.ndarray:
    flat = np.zeros(height * width, dtype=bool)
    position, value = 0, False
    for run in runs:
        flat[position:position + run] = value
        position += run
        value = not value
    if position != height * width:
        raise ValueError("游程长度与图片尺寸不符")
    return flat.reshape(height, width)


def synthetic() -> list[tuple[str, np.ndarray]]:
    def lines(*bands, width=120, slant=0.0):
        height = max(b for _, b in bands) + int(abs(slant) * width) + 1
        mask = np.zeros((height, width), dtype=bool)
        for top, bottom in bands:
            for x in range(0, width, 12):
                shift = int(slant * x)
                mask[top + shift:bottom + shift, x:x + 6] = True
        return mask

    fraction = np.zeros((90, 100), dtype=bool)
    fraction[5:35, 40:60] = True
    fraction[44:47, 10:90] = True
    fraction[55:85, 40:60] = True
    rng = np.random.default_rng(20261117)
    # 笔迹像素超过 MAX_SHEAR_PIXELS，走固定步长抽样；用放大的规则图案，文件不至于太大
    big = np.kron(lines((0, 30), (50, 80), (100, 130)), np.ones((7, 7), dtype=bool))
    return [("one_pixel", np.ones((1, 1), dtype=bool)),
            ("flat_line", np.ones((3, 200), dtype=bool)),
            ("tall_bar", np.ones((200, 4), dtype=bool)),
            ("one_line", lines((0, 30))),
            ("three_lines", lines((0, 30), (50, 80), (100, 130))),
            ("close_lines", lines((0, 30), (31, 60))),
            ("slanted", lines((0, 20), (40, 60), width=240, slant=0.15)),
            ("fraction", fraction),
            ("noise", rng.random((60, 80)) < 0.1),
            ("big_random", big)]


def real(count: int) -> list[tuple[str, np.ndarray]]:
    """标签表里各来源的真实样本。"""
    from train import formula_mask
    table = np.load(ROOT / "checkpoints" / "crop" / "labels.npz")
    cases = []
    for source in sorted(set(table["sources"].tolist())):
        rows = np.nonzero(table["sources"] == source)[0][:count]
        for r in rows.tolist():
            cases.append((f"{source}:{Path(str(table['paths'][r])).name}",
                          formula_mask(str(table["kinds"][r]), str(table["paths"][r]))))
    return cases


def main():
    ap = argparse.ArgumentParser(description=__doc__)
    ap.add_argument("--model", type=Path, default=JAVA_MODEL)
    ap.add_argument("--real-per-source", type=int, default=4)
    args = ap.parse_args()
    model = CropScale(args.model)
    cases = []
    for name, mask in synthetic() + real(args.real_per_source):
        features = ink_features(mask)
        if not np.array_equal(decode(encode(mask), *mask.shape), mask):
            raise AssertionError(f"游程编码出错: {name}")
        cases.append({"name": name, "height": int(mask.shape[0]), "width": int(mask.shape[1]),
                      "runs": encode(mask), "features": features, "choice": model.choose_index(features)})
    OUTPUT.write_text(json.dumps({"model_sha256": model.sha256, "cases": cases}), encoding="utf-8")
    print(f"{len(cases)} 个用例 -> {OUTPUT}（模型 sha {model.sha256[:12]}）")


if __name__ == "__main__":
    main()
