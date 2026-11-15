package view.glass;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

/** Reusable small warning/error capsule for inline Canvas status popups. */
public final class GlassStatusPill {
    private static final Color RED = new Color(185, 28, 28);
    private GlassStatusPill() { }

    public static void paintError(Graphics2D graphics, int width, int height) {
        if (width <= 0 || height <= 0) return;
        Graphics2D g = (Graphics2D) graphics.create();
        Glass.setUp(g);
        RoundRectangle2D shape = new RoundRectangle2D.Double(1, 1,
                Math.max(1, width - 2), Math.max(1, height - 2), height - 2, height - 2);
        g.setColor(new Color(255, 255, 255, 215));
        g.fill(shape);
        Glass.paint(g, shape, new Color(255, 59, 48), .18, 0, 0, .4);
        int cx = 17, cy = height / 2;
        g.setColor(new Color(255, 59, 48, 28));
        g.fillOval(cx - 9, cy - 9, 18, 18);
        g.setColor(RED);
        g.setStroke(new BasicStroke(1.75f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawLine(cx, cy - 5, cx, cy + 1);
        g.fillOval(cx - 1, cy + 4, 2, 2);
        g.dispose();
    }
}
