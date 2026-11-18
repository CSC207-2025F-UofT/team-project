package view.graph;

import interface_adapter.graph.GraphState;
import interface_adapter.graph.GraphTrace;

import java.awt.BasicStroke;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

// paints the whole card (header w/ function name + plot), live card + saved snapshot both use this
final class GraphCardRenderer {
    static final double BASE_WIDTH = 440;
    static final int NO_BUTTON = 0;
    static final int RESET_BUTTON = 1;
    static final int CLOSE_BUTTON = 2;

    private GraphCardRenderer() {
    }

    // everything scales with the card so zooming the canvas keeps it looking the same
    static float ui(double width) {
        return (float) Math.max(0.55, Math.min(1.8, width / BASE_WIDTH));
    }

    static double headerHeight(double w) {
        return 34 * ui(w);
    }

    static Rectangle2D plotArea(double w, double h) {
        double top = headerHeight(w);
        return new Rectangle2D.Double(0, top, w, Math.max(1, h - top));
    }

    static Rectangle2D closeButton(double w) {
        float ui = ui(w);
        double size = 22 * ui;
        return new Rectangle2D.Double(w - 8 * ui - size, (headerHeight(w) - size) / 2, size, size);
    }

    static Rectangle2D resetButton(double w) {
        Rectangle2D close = closeButton(w);
        return new Rectangle2D.Double(close.getX() - close.getWidth() - 4 * ui(w), close.getY(),
                close.getWidth(), close.getHeight());
    }

    static Rectangle2D grip(double w, double h) {
        double size = 16 * ui(w);
        return new Rectangle2D.Double(w - size, h - size, size, size);
    }

    // live = draw buttons + grip; scene may be null, then it's built here
    static void paint(Graphics2D g0, GraphState s, double w, double h, OrbitCamera camera, SurfaceScene scene,
                      GraphTrace hover, GraphTrace pinned, boolean live, int hotButton) {
        Graphics2D g = (Graphics2D) g0.create();
        try {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            float ui = ui(w);
            Shape card = new RoundRectangle2D.Double(0, 0, w - 1, h - 1, GraphTheme.ARC * ui, GraphTheme.ARC * ui);
            g.setColor(GraphTheme.CARD);
            g.fill(card);
            g.clip(card);
            Rectangle2D plot = plotArea(w, h);
            if (s.getKind() == GraphState.Kind.CURVE) {
                CurvePainter.paint(g, s, plot, ui, hover, pinned);
            } else if (s.getKind() == GraphState.Kind.SURFACE) {
                SurfacePainter.paint(g, s, scene != null ? scene : SurfaceScene.build(s, camera, plot),
                        ui, hover, pinned);
            }
            header(g, s, w, ui, live, hotButton);
            if (live) {
                grip(g, w, h, ui);
            }
            g.setClip(null);
            g.setColor(GraphTheme.BORDER);
            g.setStroke(new BasicStroke(1f));
            g.draw(card);
        } finally {
            g.dispose();
        }
    }

    private static void header(Graphics2D g, GraphState s, double w, float ui, boolean live, int hot) {
        double hh = headerHeight(w);
        g.setColor(GraphTheme.HEADER);
        g.fill(new Rectangle2D.Double(0, 0, w, hh));
        g.setColor(GraphTheme.HEADER_LINE);
        g.setStroke(new BasicStroke(1f));
        g.draw(new Line2D.Double(0, hh - 0.5, w, hh - 0.5));

        // colour dot like the notes app: orange line, ramp for a surface
        double r = 5 * ui;
        double dotX = 12 * ui;
        Ellipse2D dot = new Ellipse2D.Double(dotX, hh / 2 - r, 2 * r, 2 * r);
        if (s.getKind() == GraphState.Kind.SURFACE) {
            g.setPaint(new GradientPaint((float) dotX, (float) (hh / 2 + r), GraphTheme.surface(0, 1, 255),
                    (float) (dotX + 2 * r), (float) (hh / 2 - r), GraphTheme.surface(1, 1, 255)));
        } else {
            g.setColor(GraphTheme.CURVE);
        }
        g.fill(dot);

        // 2D / 3D badge, then the buttons on the right
        String badge = s.getKind() == GraphState.Kind.SURFACE ? "3D" : "2D";
        g.setFont(GraphTheme.bold(10 * ui));
        FontMetrics m = g.getFontMetrics();
        double right = live ? resetButton(w).getX() - 6 * ui : w - 10 * ui;
        double bw = m.stringWidth(badge) + 10 * ui;
        double bh = m.getHeight() + 2 * ui;
        double bx = right - bw;
        g.setColor(GraphTheme.BADGE);
        g.fill(new RoundRectangle2D.Double(bx, hh / 2 - bh / 2, bw, bh, bh, bh));
        g.setColor(GraphTheme.LABEL);
        g.drawString(badge, (float) (bx + 5 * ui), (float) (hh / 2 - bh / 2 + (bh - m.getHeight()) / 2 + m.getAscent()));

        double titleX = dotX + 2 * r + 8 * ui;
        GraphTitle.paint(g, s.getLatex(), titleX, hh / 2, bx - titleX - 8 * ui, 14 * ui);

        if (live) {
            button(g, resetButton(w), hot == RESET_BUTTON, ui, true);
            button(g, closeButton(w), hot == CLOSE_BUTTON, ui, false);
        }
    }

    // ↺ to recentre, × to close, drawn not font glyphs so they look the same everywhere
    private static void button(Graphics2D g, Rectangle2D b, boolean hot, float ui, boolean reset) {
        if (hot) {
            g.setColor(GraphTheme.BUTTON_HOVER);
            g.fill(new Ellipse2D.Double(b.getX(), b.getY(), b.getWidth(), b.getHeight()));
        }
        g.setColor(GraphTheme.BUTTON);
        g.setStroke(new BasicStroke(1.6f * ui, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        double cx = b.getCenterX();
        double cy = b.getCenterY();
        double k = b.getWidth() * 0.22;
        if (!reset) {
            g.draw(new Line2D.Double(cx - k, cy - k, cx + k, cy + k));
            g.draw(new Line2D.Double(cx - k, cy + k, cx + k, cy - k));
            return;
        }
        double rr = b.getWidth() * 0.26;
        g.draw(new Arc2D.Double(cx - rr, cy - rr, 2 * rr, 2 * rr, 100, 280, Arc2D.OPEN));
        double tipX = cx + rr * Math.cos(Math.toRadians(100));
        double tipY = cy - rr * Math.sin(Math.toRadians(100));
        Path2D.Double head = new Path2D.Double();
        head.moveTo(tipX + 3.2 * ui, tipY - 2.6 * ui);
        head.lineTo(tipX, tipY);
        head.lineTo(tipX + 3.4 * ui, tipY + 2.4 * ui);
        g.draw(head);
    }

    private static void grip(Graphics2D g, double w, double h, float ui) {
        g.setColor(GraphTheme.AXIS);
        g.setStroke(new BasicStroke(1.2f * ui, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        for (int i = 1; i <= 3; i++) {
            double d = (3 + 3.5 * i) * ui;
            g.draw(new Line2D.Double(w - d, h - 3 * ui, w - 3 * ui, h - d));
        }
    }
}
