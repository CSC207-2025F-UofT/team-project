package data_access;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Same cases and numbers as {@code src/neutral network/tests/test_formula_layout.py}; keep them in step. */
class FormulaLayoutTest {

    private static FormulaLayout.Size layout(double inkWidth, double inkHeight, List<int[]> spans) {
        return FormulaLayout.size(inkWidth, inkHeight,
                FormulaLayout.tallestBand(spans, inkHeight, FormulaLayout.GAP_MIN_PX));
    }

    private static List<int[]> spans(int... topsAndBottoms) {
        List<int[]> spans = new java.util.ArrayList<>();
        for (int i = 0; i < topsAndBottoms.length; i += 2) {
            spans.add(new int[]{topsAndBottoms[i], topsAndBottoms[i + 1]});
        }
        return spans;
    }

    private static void assertSize(FormulaLayout.Size size, double scale, int width, int height) {
        assertEquals(scale, size.scale(), 1e-12);
        assertEquals(width, size.width(), "width");
        assertEquals(height, size.height(), "height");
    }

    @Test
    void singleLineKeepsTheTallestBandAt112AndTheCanvasAt128() {
        assertSize(layout(300, 40, spans(0, 40)), 2.8, 864, 128);
    }

    @Test
    void twoLinesKeepTheSingleLineScaleAndGrowTaller() {
        assertSize(layout(200, 110, spans(0, 40, 70, 110)), 2.8, 576, 336);
    }

    @Test
    void fractionSplitIntoThreeBandsIsScaledByItsNumerator() {
        assertSize(layout(100, 103, spans(0, 40, 50, 53, 63, 103)), 2.8, 304, 320);
    }

    @Test
    void closeStretchesOfInkAreOneBand() {
        assertSize(layout(100, 80, spans(0, 40, 41, 80)), 1.4, 160, 128);
    }

    @Test
    void longFlatLineIsNotShrunkByTheOldWidthRule() {
        assertSize(layout(1000, 4, spans(0, 4)), 2.24, 2256, 128);
    }

    @Test
    void oversizedFormulaIsShrunkAsAWholeToTheLimits() {
        FormulaLayout.Size size = layout(300, 180, spans(0, 40, 70, 110, 140, 180));
        assertSize(size, 2.3344937339644183, 720, 448);
        assertTrue(size.height() <= FormulaLayout.MAX_H);
        assertTrue((size.height() / FormulaLayout.STRIDE) * (size.width() / FormulaLayout.STRIDE)
                <= FormulaLayout.MAX_TOKENS);
    }

    @Test
    void rowSpansAndRoundingHelpers() {
        List<int[]> found = FormulaLayout.rowSpans(new boolean[]{false, true, true, false, false, true, true, true});
        assertEquals(2, found.size());
        assertArrayEquals(new int[]{1, 3}, found.get(0));
        assertArrayEquals(new int[]{5, 8}, found.get(1));
        assertTrue(FormulaLayout.rowSpans(new boolean[0]).isEmpty());
        assertArrayEquals(new int[]{0, 2}, FormulaLayout.rowSpans(new boolean[]{true, true}).get(0));
        assertEquals(3, FormulaLayout.roundHalfUp(2.5));
        assertEquals(4, FormulaLayout.roundHalfUp(3.5));
        assertEquals(128, FormulaLayout.snapUp(128.0000000001));
        assertEquals(144, FormulaLayout.snapUp(128.1));
    }
}
