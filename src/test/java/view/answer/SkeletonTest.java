package view.answer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SkeletonTest {
    private static final int W = 40;
    private static final int H = 40;

    private static void line(boolean[] g, int x1, int y1, int x2, int y2) {
        int n = Math.max(Math.abs(x2 - x1), Math.abs(y2 - y1));
        for (int i = 0; i <= n; i++) {
            g[(y1 + (y2 - y1) * i / n) * W + x1 + (x2 - x1) * i / n] = true;
        }
    }

    @Test
    void spurIntoCornerBecomesPoint() {
        // spur off a fork out to the tip, like the middle of a 3
        boolean[] g = new boolean[W * H];
        line(g, 20, 20, 34, 6);
        line(g, 20, 20, 34, 34);
        line(g, 20, 20, 14, 20);
        boolean[] out = Skeleton.sharpen(g, W, H, 8);
        assertTrue(out[20 * W + 14], "tip stays");
        assertFalse(out[20 * W + 20], "fork gone");
        assertFalse(out[20 * W + 17], "spur gone");
        assertTrue(out[6 * W + 34] && out[34 * W + 34], "arm ends stay");
        assertTrue(neighbours(out, 14, 20) == 2, "both arms run into the tip");
    }

    @Test
    void sideTwigStays() {
        // overshoot sticking out sideways, like the bar of an H
        boolean[] g = new boolean[W * H];
        line(g, 5, 20, 35, 20);
        line(g, 20, 21, 20, 23);
        boolean[] out = Skeleton.sharpen(g, W, H, 5);
        assertTrue(out[23 * W + 20] && out[21 * W + 20]);
        assertTrue(out[20 * W + 20]);
    }

    @Test
    void noDustLeft() {
        boolean[] g = new boolean[W * H];
        line(g, 20, 20, 34, 6);
        line(g, 20, 20, 34, 34);
        int[][] stairs = {{19, 20}, {18, 20}, {18, 21}, {17, 21}, {16, 21}, {16, 22}, {15, 22}, {14, 22}};
        for (int[] p : stairs) {
            g[p[1] * W + p[0]] = true;  // stair corners the walk skips
        }
        boolean[] out = Skeleton.sharpen(g, W, H, 8);
        for (int y = 1; y < H - 1; y++) {
            for (int x = 1; x < W - 1; x++) {
                assertTrue(!out[y * W + x] || neighbours(out, x, y) > 0, "lone px at " + x + "," + y);
            }
        }
    }

    private static int neighbours(boolean[] g, int x, int y) {
        int n = 0;
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                n += (dx != 0 || dy != 0) && g[(y + dy) * W + x + dx] ? 1 : 0;
            }
        }
        return n;
    }

    @Test
    void longArmStays() {
        boolean[] g = new boolean[W * H];
        line(g, 5, 20, 35, 20);
        line(g, 20, 21, 20, 35);  // real arm, way past the limit
        boolean[] out = Skeleton.sharpen(g, W, H, 5);
        assertTrue(out[35 * W + 20]);
    }

    @Test
    void shortStrokeOnItsOwnStays() {
        boolean[] g = new boolean[W * H];
        line(g, 10, 10, 13, 10);  // like a dot or tiny dash
        boolean[] out = Skeleton.sharpen(g, W, H, 5);
        assertTrue(out[10 * W + 10] && out[10 * W + 13]);
    }
}
