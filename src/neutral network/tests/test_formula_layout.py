"""版面规则（formula_layout.py）的测试。期望数值与 Java 的 FormulaLayoutTest 相同，两边必须一致。"""
import sys
import unittest
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
from formula_layout import (GAP_MIN_PX, MAX_H, MAX_TOKENS, STRIDE, formula_layout, round_half_up,
                            row_spans, snap_up, tallest_band)


def layout(ink_width, ink_height, spans, min_gap=GAP_MIN_PX):
    return formula_layout(ink_width, ink_height, tallest_band(spans, ink_height, min_gap))


class FormulaLayoutTests(unittest.TestCase):
    def test_single_line_keeps_the_tallest_band_at_112_and_the_canvas_at_128(self):
        result = layout(300, 40, [(0, 40)])
        self.assertAlmostEqual(result.scale, 2.8)
        self.assertEqual((result.width, result.height), (864, 128))

    def test_two_lines_keep_the_single_line_scale_and_grow_taller(self):
        result = layout(200, 110, [(0, 40), (70, 110)])
        self.assertAlmostEqual(result.scale, 2.8)
        self.assertEqual((result.width, result.height), (576, 336))

    def test_stroke_coordinates_use_the_same_rule_without_a_pixel_gap_floor(self):
        result = layout(200, 110, [(0.0, 40.0), (70.0, 110.0)], min_gap=0.0)
        self.assertEqual((result.width, result.height), (576, 336))

    def test_fraction_split_into_three_bands_is_scaled_by_its_numerator(self):
        result = layout(100, 103, [(0, 40), (50, 53), (63, 103)])
        self.assertAlmostEqual(result.scale, 2.8)
        self.assertEqual((result.width, result.height), (304, 320))

    def test_close_stretches_of_ink_are_one_band(self):
        result = layout(100, 80, [(0, 40), (41, 80)])
        self.assertAlmostEqual(result.scale, 1.4)
        self.assertEqual((result.width, result.height), (160, 128))

    def test_long_flat_line_is_not_shrunk_by_the_old_width_rule(self):
        result = layout(1000, 4, [(0, 4)])
        self.assertAlmostEqual(result.scale, 2.24)
        self.assertEqual((result.width, result.height), (2256, 128))

    def test_oversized_formula_is_shrunk_as_a_whole_to_the_limits(self):
        result = layout(300, 180, [(0, 40), (70, 110), (140, 180)])
        self.assertAlmostEqual(result.scale, 2.3344937339644183)
        self.assertEqual((result.width, result.height), (720, 448))
        self.assertLessEqual(result.height, MAX_H)
        self.assertLessEqual(result.height // STRIDE * (result.width // STRIDE), MAX_TOKENS)

    def test_sizes_are_multiples_of_the_stride(self):
        for ink_width, ink_height in [(1, 1), (37, 91), (640, 130), (50, 400)]:
            result = layout(ink_width, ink_height, [(0, ink_height)])
            self.assertEqual(result.width % STRIDE, 0)
            self.assertEqual(result.height % STRIDE, 0)
            self.assertGreaterEqual(result.height, 128)
            self.assertGreaterEqual(result.width, 64)

    def test_row_spans_and_rounding_helpers(self):
        self.assertEqual(row_spans([0, 1, 1, 0, 0, 1, 1, 1]), [(1, 3), (5, 8)])
        self.assertEqual(row_spans([]), [])
        self.assertEqual(row_spans([1, 1]), [(0, 2)])
        self.assertEqual(round_half_up(2.5), 3)
        self.assertEqual(round_half_up(3.5), 4)
        self.assertEqual(snap_up(128.0000000001), 128)
        self.assertEqual(snap_up(128.1), 144)


if __name__ == '__main__':
    unittest.main()
