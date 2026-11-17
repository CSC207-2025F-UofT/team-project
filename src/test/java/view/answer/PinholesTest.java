package view.answer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PinholesTest {
    private static final int W = 40;
    private static final int H = 40;

    @Test
    void pinholeGetsFilled() {
        boolean[] g = block(5, 5, 30, 30);
        g[15 * W + 15] = false;  // aa gap at a sharp notch
        boolean[] out = Pinholes.fill(g, W, H, 4);
        assertTrue(out[15 * W + 15]);
    }

    @Test
    void realHoleStays() {
        boolean[] g = block(5, 5, 30, 30);
        for (int y = 12; y < 20; y++) {
            for (int x = 12; x < 20; x++) {
                g[y * W + x] = false;  // counter of an o
            }
        }
        boolean[] out = Pinholes.fill(g, W, H, 4);
        assertFalse(out[15 * W + 15]);
        assertFalse(out[0], "outside stays empty");
    }

    private static boolean[] block(int x1, int y1, int x2, int y2) {
        boolean[] g = new boolean[W * H];
        for (int y = y1; y < y2; y++) {
            for (int x = x1; x < x2; x++) {
                g[y * W + x] = true;
            }
        }
        return g;
    }
}
