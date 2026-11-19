"""缩放模型的测试：crop_scale.py（特征、树模型、导出）、crop.py 的选择逻辑、train.py 的接入，
以及 band_scale / band_heights 接口。不需要识别模型权重。"""
import json
import sys
import tempfile
import unittest
from pathlib import Path

import numpy as np
from PIL import Image

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
sys.path.insert(0, str(Path(__file__).resolve().parent))
import crop
import crop_scale
import train
from formula_layout import band_heights, tallest_band
from generate_crop_scale_fixtures import JAVA_MODEL, OUTPUT as FIXTURES, decode, encode
from preprocess import formula_ink_mask, load_formula_image


def lines(*bands, width=120):
    """每个 (上, 下) 画一条横向的"字"带（竖条交替，像一行字符）。"""
    height = max(bottom for _, bottom in bands)
    mask = np.zeros((height, width), dtype=bool)
    for top, bottom in bands:
        for x in range(0, width, 12):
            mask[top:bottom, x:x + 6] = True
    return mask


def export(method: str, X: np.ndarray, nll: np.ndarray, folder: str):
    booster = crop.fit_booster(method, X, nll)
    path = Path(folder) / f"{method}.json"
    names = [f"f{i}" for i in range(X.shape[1])]
    crop_scale.save_model(path, method, names, crop_scale.trees_from_lightgbm(booster), {})
    return booster, crop_scale.CropScale(path), names


class BandHeightTests(unittest.TestCase):
    def test_band_heights_lists_every_band_and_tallest_band_is_its_maximum(self):
        spans = [(0, 40), (70, 100), (102, 110)]
        self.assertEqual(band_heights(spans, 110), [40, 40])            # 2 像素空白 < 3%，合并
        self.assertEqual(band_heights(spans, 110, gap_fraction=0.01), [40, 30, 8])
        self.assertEqual(tallest_band(spans, 110), 40)
        self.assertEqual(tallest_band([], 10), 0.0)


class HelperTests(unittest.TestCase):
    """与 Java CropScaleTest.helpersFollowTheirPythonDefinitions 相同的数值。"""

    def test_helpers(self):
        self.assertEqual(crop_scale.percentile([4, 1, 3, 2], 50), 2.5)
        self.assertEqual(crop_scale.percentile([], 50), 0.0)
        self.assertEqual(crop_scale.count_peaks(np.array([0, 5, 5, 0]), 1), 1)
        self.assertEqual(crop_scale.count_peaks(np.array([1, 5, 1, 4, 1]), 2), 2)
        self.assertEqual(crop_scale.count_peaks(np.array([1, 5, 4, 4.5, 1]), 2), 1)
        np.testing.assert_array_equal(crop_scale.moving_average(np.array([3, 6, 9]), 2), [1.5, 4.5, 7.5])
        diagonal = np.eye(3, dtype=bool)
        self.assertEqual(len(crop_scale.components(diagonal)), 1)
        self.assertEqual(len(crop_scale.components(np.array([[True, False, True]]))), 2)

    def test_autocorrelation_finds_the_line_pitch(self):
        profile = np.tile([10, 10, 10, 0, 0, 0, 0, 0, 0, 0], 4)   # 每 10 行一行字
        lag, peak = crop_scale.autocorrelation_peak(profile)
        self.assertAlmostEqual(lag, 10 / 40)
        self.assertGreater(peak, 0.5)


class FeatureTests(unittest.TestCase):
    def test_features_have_fixed_names_and_are_finite(self):
        a = crop_scale.ink_features(lines((0, 30)))
        b = crop_scale.ink_features(lines((0, 30), (50, 80), (100, 130)))
        self.assertEqual(list(a), list(b))
        self.assertTrue(np.isfinite(list(a.values())).all())
        self.assertTrue(np.isfinite(list(b.values())).all())

    def test_band_features_count_lines_and_are_resolution_independent(self):
        three = crop_scale.ink_features(lines((0, 30), (50, 80), (100, 130)))
        self.assertEqual(three["bands@0.03"], 3)
        self.assertAlmostEqual(three["band1@0.03"], 30 / 130)
        big = crop_scale.ink_features(np.kron(lines((0, 30), (50, 80), (100, 130)), np.ones((2, 2), dtype=bool)))
        for name in ("bands@0.03", "band1@0.03", "rule_band", "log_aspect", "density"):
            self.assertAlmostEqual(three[name], big[name], places=6, msg=name)

    def test_fraction_bar_is_detected(self):
        mask = np.zeros((90, 100), dtype=bool)
        mask[5:35, 40:60] = True     # 分子
        mask[44:47, 10:90] = True    # 分数线
        mask[55:85, 40:60] = True    # 分母
        self.assertEqual(crop_scale.ink_features(mask)["fraction_bars"], 1)
        self.assertEqual(crop_scale.ink_features(lines((0, 30)))["fraction_bars"], 0)

    def test_huge_masks_use_a_fixed_stride_and_are_deterministic(self):
        mask = np.random.default_rng(0).random((700, 600)) < 0.6
        self.assertEqual(crop_scale.ink_features(mask), crop_scale.ink_features(mask.copy()))


class ModelTests(unittest.TestCase):
    def test_soft_target_follows_the_lowest_loss(self):
        nll = np.full((2, len(crop_scale.CANDIDATES)), 3.0)
        nll[0, crop_scale.RULE_INDEX] = 1.0
        nll[1, -1] = 1.0
        np.testing.assert_allclose(crop.soft_target(nll), [0.0, crop_scale.LOG2_CANDIDATES[-1]], atol=1e-6)

    def test_exported_trees_choose_exactly_like_lightgbm(self):
        rng = np.random.default_rng(0)
        X = rng.uniform(size=(400, 3))
        best = np.where(X[:, 0] > 0.5, crop_scale.RULE_INDEX + 4, crop_scale.RULE_INDEX)   # 第 0 个特征决定最佳 m
        nll = 1.0 + 0.1 * np.abs(np.arange(len(crop_scale.CANDIDATES))[None, :] - best[:, None])
        with tempfile.TemporaryDirectory() as folder:
            for method in ("surface", "direct"):
                booster, exported, names = export(method, X[:300], nll[:300], folder)
                lgb = crop.choose(method, booster, X[300:])
                ours = [exported.choose_index(dict(zip(names, row))) for row in X[300:].tolist()]
                np.testing.assert_array_equal(lgb, ours)
                self.assertGreater((lgb == best[300:]).mean(), 0.9, method)
                saved = json.loads((Path(folder) / f"{method}.json").read_text(encoding="utf-8"))
                self.assertEqual(saved["log2_candidates"], crop_scale.LOG2_CANDIDATES.tolist())


@unittest.skipUnless(FIXTURES.is_file() and JAVA_MODEL.is_file(), "没有对照数据或 Java 端模型")
class FixtureTests(unittest.TestCase):
    """对照数据由 generate_crop_scale_fixtures.py 生成；Python 改动特征后这里会先失败，提醒同步 Java。"""

    def test_fixtures_still_match_python(self):
        data = json.loads(FIXTURES.read_text(encoding="utf-8"))
        model = crop_scale.CropScale(JAVA_MODEL)
        self.assertEqual(data["model_sha256"], model.sha256, "Java 端模型换了，请重新生成对照数据")
        for case in data["cases"]:
            mask = decode(case["runs"], case["height"], case["width"])
            self.assertEqual(encode(mask), case["runs"])
            features = crop_scale.ink_features(mask)
            self.assertEqual(features, case["features"], case["name"])
            self.assertEqual(model.choose_index(features), case["choice"], case["name"])


class TrainingTests(unittest.TestCase):
    def picture(self, folder: str) -> Path:
        path = Path(folder) / "line.png"
        Image.fromarray(np.where(lines((10, 50), width=200), 0, 255).astype(np.uint8)).convert("RGB").save(path)
        return path

    def test_band_scale_changes_the_scale_and_defaults_to_the_rule(self):
        with tempfile.TemporaryDirectory() as folder:
            path = self.picture(folder)
            self.assertEqual(formula_ink_mask(path).shape, (40, 198))
            rule = load_formula_image(path, invert=False)
            self.assertTrue((rule == load_formula_image(path, invert=False, band_scale=1.0)).all())
            smaller = load_formula_image(path, invert=False, band_scale=2.0)
            self.assertLess(smaller.shape[-1], rule.shape[-1])                  # m > 1 字变小
            self.assertEqual(rule.shape[-2] % 16, 0)

    def test_dataset_uses_the_scale_model_and_the_cache(self):
        with tempfile.TemporaryDirectory() as folder:
            path = self.picture(folder)
            (Path(folder) / "train.jsonl").write_text(json.dumps({"image": path.name, "latex": "x"}) + "\n",
                                                      encoding="utf-8")
            vocab = {t: i for i, t in enumerate(train.load_vocab())}

            class Fixed:                                           # 总是选 m=2 的假模型
                def band_scale(self, mask):
                    return 2.0

            plain = train.MathWriting(folder, ["train"], vocab, train=False)
            scaled = train.MathWriting(folder, ["train"], vocab, train=False, crop_scale=Fixed())
            self.assertEqual(plain.band_scale(0), 1.0)
            self.assertEqual(scaled.band_scale(0), 2.0)
            self.assertTrue((scaled[0][0] == load_formula_image(path, invert=False, band_scale=2.0)).all())
            cached = train.MathWriting(folder, ["train"], vocab, train=False, crop_scale=Fixed(),
                                       scale_cache={str(path.resolve()): crop_scale.CANDIDATES[0]})
            self.assertEqual(cached.band_scale(0), crop_scale.CANDIDATES[0])   # 缓存优先

            cache_file = Path(folder) / "cache.npz"
            train.save_scale_cache(cache_file, {"a": crop_scale.CANDIDATES[3], "b": 1.0})
            self.assertEqual(train.load_scale_cache(cache_file), {"a": crop_scale.CANDIDATES[3], "b": 1.0})


if __name__ == "__main__":
    unittest.main()
