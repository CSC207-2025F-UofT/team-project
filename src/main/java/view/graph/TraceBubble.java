package view.graph;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

// the dark readout bubble + the little value pills on the axes
final class TraceBubble {
    private TraceBubble() {
    }

    // bubble up-right of (ax, ay), flips to stay inside bounds
    static void draw(Graphics2D g, double ax, double ay, String caption, String text, float ui, Rectangle2D bounds) {
        Font capFont = GraphTheme.font(11 * ui);
        Font textFont = GraphTheme.bold(13 * ui);
        FontMetrics cap = g.getFontMetrics(capFont);
        FontMetrics main = g.getFontMetrics(textFont);
        double pad = 8 * ui;
        double w = Math.max(main.stringWidth(text), caption == null ? 0 : cap.stringWidth(caption)) + 2 * pad;
        double h = main.getHeight() + (caption == null ? 0 : cap.getHeight()) + 1.2 * pad;
        double gap = 12 * ui;
        double x = ax + gap;
        double y = ay - h - gap;
        if (x + w > bounds.getMaxX() - 4) {
            x = ax - w - gap;
        }
        if (y < bounds.getMinY() + 4) {
            y = ay + gap;
        }
        x = clamp(x, bounds.getMinX() + 4, bounds.getMaxX() - w - 4);
        y = clamp(y, bounds.getMinY() + 4, bounds.getMaxY() - h - 4);
        g.setColor(GraphTheme.BUBBLE);
        g.fill(new RoundRectangle2D.Double(x, y, w, h, 10 * ui, 10 * ui));
        double baseline = y + 0.6 * pad + main.getAscent();
        if (caption != null) {
            g.setFont(capFont);
            g.setColor(GraphTheme.BUBBLE_CAPTION);
            g.drawString(caption, (float) (x + pad), (float) (y + 0.6 * pad + cap.getAscent()));
            baseline += cap.getHeight();
        }
        g.setFont(textFont);
        g.setColor(GraphTheme.BUBBLE_TEXT);
        g.drawString(text, (float) (x + pad), (float) baseline);
    }

    // small value tag centred on (cx, cy), kept inside bounds
    static void pill(Graphics2D g, double cx, double cy, String text, float ui, Rectangle2D bounds) {
        Font font = GraphTheme.bold(10.5f * ui);
        FontMetrics m = g.getFontMetrics(font);
        double w = m.stringWidth(text) + 10 * ui;
        double h = m.getHeight() + 2 * ui;
        double x = clamp(cx - w / 2, bounds.getMinX() + 2, bounds.getMaxX() - w - 2);
        double y = clamp(cy - h / 2, bounds.getMinY() + 2, bounds.getMaxY() - h - 2);
        g.setColor(GraphTheme.BUBBLE);
        g.fill(new RoundRectangle2D.Double(x, y, w, h, h, h));
        g.setFont(font);
        g.setColor(GraphTheme.BUBBLE_TEXT);
        g.drawString(text, (float) (x + 5 * ui), (float) (y + (h - m.getHeight()) / 2 + m.getAscent()));
    }

    static double clamp(double v, double lo, double hi) {
        return hi < lo ? lo : Math.max(lo, Math.min(hi, v));
    }
}
