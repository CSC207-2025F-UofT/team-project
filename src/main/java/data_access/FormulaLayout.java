package data_access;

import java.util.ArrayList;
import java.util.List;

/**
 * Scale and canvas size of a formula picture. The same rule as
 * {@code src/neutral network/formula_layout.py}, which training uses: keep the two in step.
 *
 * <p>The ink is cut into horizontal bands (two stretches of ink with enough white between them are
 * two bands). The scale makes the tallest band {@value #LINE_PX} px high, so a single line comes out
 * as before, while a formula of several lines keeps every line the same size and the canvas grows
 * taller instead. Only a canvas over {@value #MAX_H} px high or over {@value #MAX_TOKENS} feature
 * cells is shrunk, as a whole.
 */
final class FormulaLayout {
    static final int LINE_PX = 112;
    static final int MARGIN = 8;
    static final int STRIDE = 16;
    static final int MIN_W = 64;
    static final int MIN_H = 128;
    static final int MAX_H = 512;
    static final int MAX_TOKENS = 1280;
    static final double GAP_FRACTION = 0.03;
    static final int GAP_MIN_PX = 2;
    static final double FLAT_FRACTION = 0.05;
    private static final double SHRINK_STEP = 0.98;

    record Size(double scale, int width, int height) {
    }

    private FormulaLayout() {
    }

    /** Round half up, like Python's formula_layout.round_half_up. */
    static int roundHalfUp(double value) {
        return (int) Math.floor(value + 0.5);
    }

    /** Round up to a multiple of {@value #STRIDE}; the tolerance keeps float error from adding a cell. */
    static int snapUp(double value) {
        return (int) Math.ceil(value / STRIDE - 1e-9) * STRIDE;
    }

    /** Which rows hold ink -> runs of such rows as {first row, row after the last}. */
    static List<int[]> rowSpans(boolean[] occupied) {
        List<int[]> spans = new ArrayList<>();
        int start = -1;
        for (int y = 0; y < occupied.length; y++) {
            if (occupied[y] && start < 0) {
                start = y;
            } else if (!occupied[y] && start >= 0) {
                spans.add(new int[]{start, y});
                start = -1;
            }
        }
        if (start >= 0) {
            spans.add(new int[]{start, occupied.length});
        }
        return spans;
    }

    /**
     * Height of the tallest band. {@code spans} are {top, bottom (not included)} in ascending order;
     * two of them join into one band when the white between them is under
     * max({@value #GAP_FRACTION} * inkHeight, minGap).
     */
    static double tallestBand(List<int[]> spans, double inkHeight, double minGap) {
        double gap = Math.max(GAP_FRACTION * inkHeight, minGap);
        double best = 0;
        double start = 0;
        double end = 0;
        boolean open = false;
        for (int[] span : spans) {
            if (!open) {
                start = span[0];
                end = span[1];
                open = true;
            } else if (span[0] - end < gap) {
                end = Math.max(end, span[1]);
            } else {
                best = Math.max(best, end - start);
                start = span[0];
                end = span[1];
            }
        }
        return open ? Math.max(best, end - start) : best;
    }

    /** Ink size (stroke width included) and its tallest band -> scale and canvas size. */
    static Size size(double inkWidth, double inkHeight, double tallest) {
        double base = Math.max(tallest, FLAT_FRACTION * inkWidth);
        double scale = LINE_PX / base;
        while (true) {
            int width = Math.max(MIN_W, snapUp(inkWidth * scale + 2 * MARGIN));
            int height = Math.max(MIN_H, snapUp(inkHeight * scale + 2 * MARGIN));
            if (height <= MAX_H && (height / STRIDE) * (width / STRIDE) <= MAX_TOKENS) {
                return new Size(scale, width, height);
            }
            scale *= SHRINK_STEP;
        }
    }
}
