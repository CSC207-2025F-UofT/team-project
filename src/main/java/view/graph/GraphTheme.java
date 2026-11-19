package view.graph;

import view.stack.Fonts;

import java.awt.Color;
import java.awt.Font;

// colours + fonts for the graph card, light notes-app look
final class GraphTheme {
    static final Color CARD = Color.WHITE;
    static final Color HEADER = new Color(247, 247, 249);
    static final Color HEADER_LINE = new Color(228, 228, 233);
    static final Color BORDER = new Color(210, 212, 218);
    static final Color GRID_MINOR = new Color(243, 243, 246);
    static final Color GRID_MAJOR = new Color(226, 227, 233);
    static final Color AXIS = new Color(140, 140, 146);
    static final Color LABEL = new Color(118, 118, 124);
    static final Color TITLE = new Color(28, 28, 30);
    static final Color CURVE = new Color(255, 149, 0);
    static final Color BUBBLE = new Color(30, 30, 32, 236);
    static final Color BUBBLE_TEXT = Color.WHITE;
    static final Color BUBBLE_CAPTION = new Color(255, 255, 255, 165);
    static final Color GUIDE = new Color(120, 120, 128, 150);
    static final Color BUTTON = new Color(110, 110, 118);
    static final Color BUTTON_HOVER = new Color(0, 0, 0, 20);
    static final Color BADGE = new Color(232, 232, 237);
    static final Color FLOOR = new Color(236, 237, 242);
    // phase portraits
    static final Color PHASE = new Color(88, 86, 214);
    static final Color SLOPE = new Color(88, 86, 214, 150);
    static final Color ATTRACTOR = new Color(52, 199, 89);
    static final Color REPELLER = new Color(255, 59, 48);
    static final Color SADDLE = new Color(255, 149, 0);
    static final Color CENTER = new Color(0, 122, 255);
    static final Color NEUTRAL = new Color(142, 142, 147);
    private static final Color[] SPEED = {
            new Color(120, 130, 225), new Color(150, 90, 220), new Color(232, 62, 120)};

    // surface height ramp, low -> high
    private static final float[] STOPS = {0f, 0.35f, 0.68f, 1f};
    private static final Color[] RAMP = {
            new Color(48, 92, 222), new Color(20, 172, 214), new Color(88, 204, 120), new Color(255, 206, 40)};

    static final float ARC = 16f;

    private GraphTheme() {
    }

    static Font font(float size) {
        return Fonts.ui(Font.PLAIN, size);
    }

    static Font bold(float size) {
        return Fonts.ui(Font.BOLD, size);
    }

    static Font italic(float size) {
        return Fonts.ui(Font.ITALIC, size);
    }

    // arrow colour by how fast the field is there, 0 slow -> 1 fast
    static Color speed(double t, int alpha) {
        double s = Math.max(0, Math.min(1, t)) * (SPEED.length - 1);
        int k = Math.min(SPEED.length - 2, (int) s);
        double f = s - k;
        Color a = SPEED[k];
        Color b = SPEED[k + 1];
        return new Color(mix(a.getRed(), b.getRed(), f, 1), mix(a.getGreen(), b.getGreen(), f, 1),
                mix(a.getBlue(), b.getBlue(), f, 1), alpha);
    }

    // level 0..1 -> ramp colour, light 0..1 darkens it
    static Color surface(double level, double light, int alpha) {
        double t = Math.max(0, Math.min(1, level));
        int k = 0;
        while (k < STOPS.length - 2 && t > STOPS[k + 1]) {
            k++;
        }
        double f = (t - STOPS[k]) / (STOPS[k + 1] - STOPS[k]);
        Color a = RAMP[k];
        Color b = RAMP[k + 1];
        double l = Math.max(0, Math.min(1, light));
        return new Color(mix(a.getRed(), b.getRed(), f, l), mix(a.getGreen(), b.getGreen(), f, l),
                mix(a.getBlue(), b.getBlue(), f, l), alpha);
    }

    private static int mix(int a, int b, double f, double light) {
        return (int) Math.round(Math.max(0, Math.min(255, (a + (b - a) * f) * light)));
    }
}
