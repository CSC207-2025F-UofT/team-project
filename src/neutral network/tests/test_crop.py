"""crop.py 的特征、LightGBM 选择逻辑，以及 band_scale / band_heights 接口的测试。不需要识别模型权重。"""
import sys
import tempfile
import unittest
from pathlib import Path

import numpy as np
from PIL import Image

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
import crop
from formula_layout import band_heights, tallest_band
from preprocess import formula_ink_mask, load_formula_image


def lines(*bands, width=120):
    """每个 (上, 下) 画一条横向的"字"带（竖条交替，像一行字符）。"""
    height = max(bottom for _, bottom in bands)
    mask = np.zeros((height, width), dtype=bool)
    for top, bottom in bands:
        for x in range(0, width, 12):
            mask[top:bottom, x:x + 6] = True
    return mask


class BandHeightTests(unittest.TestCase):
    def test_band_heights_lists_every_band_and_tallest_band_is_its_maximum(self):
        spans = [(0, 40), (70, 100), (102, 110)]
        self.assertEqual(band_heights(spans, 110), [40, 40])            # 2 像素空白 < 3%，合并
        self.assertEqual(band_heights(spans, 110, gap_fraction=0.01), [40, 30, 8])
        self.assertEqual(tallest_band(spans, 110), 40)
        self.assertEqual(tallest_band([], 10), 0.0)


class FeatureTests(unittest.TestCase):
    def test_features_have_fixed_names_and_are_finite(self):
        a, b = crop.ink_features(lines((0, 30))), crop.ink_features(lines((0, 30), (50, 80), (100, 130)))
        self.assertEqual(list(a), list(b))
        self.assertTrue(np.isfinite(list(a.values())).all())
        self.assertTrue(np.isfinite(list(b.values())).all())

    def test_band_features_count_lines_and_are_resolution_independent(self):
        three = crop.ink_features(lines((0, 30), (50, 80), (100, 130)))
        self.assertEqual(three["bands@0.03"], 3)
        self.assertAlmostEqual(three["band1@0.03"], 30 / 130)
        big = crop.ink_features(np.kron(lines((0, 30), (50, 80), (100, 130)), np.ones((2, 2), dtype=bool)))
        for name in ("bands@0.03", "band1@0.03", "rule_band", "log_aspect", "density"):
            self.assertAlmostEqual(three[name], big[name], places=6, msg=name)

    def test_fraction_bar_is_detected(self):
        mask = np.zeros((90, 100), dtype=bool)
        mask[5:35, 40:60] = True     # 分子
        mask[44:47, 10:90] = True    # 分数线
        mask[55:85, 40:60] = True    # 分母
        self.assertEqual(crop.ink_features(mask)["fraction_bars"], 1)
        self.assertEqual(crop.ink_features(lines((0, 30)))["fraction_bars"], 0)


class ChoiceTests(unittest.TestCase):
    def test_soft_target_follows_the_lowest_loss(self):
        nll = np.full((2, len(crop.CANDIDATES)), 3.0)
        nll[0, crop.RULE_INDEX] = 1.0
        nll[1, -1] = 1.0
        np.testing.assert_allclose(crop.soft_target(nll), [0.0, crop.LOG2_CANDIDATES[-1]], atol=1e-6)

    def test_surface_model_learns_a_per_sample_best_scale(self):
        rng = np.random.default_rng(0)
        X = rng.uniform(size=(400, 3))
        best = np.where(X[:, 0] > 0.5, crop.RULE_INDEX + 4, crop.RULE_INDEX)   # 第 0 个特征决定最佳 m
        nll = 1.0 + 0.1 * np.abs(np.arange(len(crop.CANDIDATES))[None, :] - best[:, None])
        for method in ("surface", "direct"):
            picks = crop.choose(method, crop.fit_booster(method, X[:300], nll[:300]), X[300:])
            self.assertGreater((picks == best[300:]).mean(), 0.9, method)


class BandScaleTests(unittest.TestCase):
    def test_band_scale_changes_the_scale_and_defaults_to_the_rule(self):
        with tempfile.TemporaryDirectory() as folder:
            path = Path(folder) / "line.png"
            Image.fromarray(np.where(lines((10, 50), width=200), 0, 255).astype(np.uint8)).convert("RGB").save(path)
            self.assertEqual(formula_ink_mask(path).shape, (40, 198))
            rule = load_formula_image(path, invert=False)
            self.assertTrue((rule == load_formula_image(path, invert=False, band_scale=1.0)).all())
            smaller = load_formula_image(path, invert=False, band_scale=2.0)
            self.assertLess(smaller.shape[-1], rule.shape[-1])                  # m > 1 字变小
            self.assertEqual(rule.shape[-2] % 16, 0)


if __name__ == "__main__":
    unittest.main()
