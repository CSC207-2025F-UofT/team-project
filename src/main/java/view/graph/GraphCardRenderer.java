package view.graph;

import interface_adapter.graph.GraphState;
import interface_adapter.graph.GraphTrace;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
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
import java.awt.image.BufferedImage;

// paints the whole card (header w/ function name + plot), live cards + saved snapshots all use this
final class GraphCardRenderer {
    static final double BASE_WIDTH = 440;
    static final int NO_BUTTON = 0;
    static final int RESET_BUTTON = 1;
    static final int CLOSE_BUTTON = 2;

    // measures header text off-screen so layout and painting agree without a live Graphics
    private static final Graphics2D MEASURE = measure();

    private GraphCardRenderer() {
    }

    private static Graphics2D measure() {
        Graphics2D g = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        return g;
    }

    /**
     * Where things go in the header, left to right: dot, title, the live x / y boxes (if any), badge,
     * ↺ ×. {@code controls} is null when the boxes did not fit (or there are none); then the title
     * gets the whole width, as it does on a saved picture.
     */
    record HeaderLayout(double titleX, double titleWidth, Rectangle2D controls, double badgeX, double badgeWidth) {
    }

    static HeaderLayout headerLayout(String latex, String badge, double w, boolean live, Dimension controls) {
        float ui = ui(w);
        double hh = headerHeight(w);
        double titleX = 12 * ui + 10 * ui + 8 * ui;   // dot inset + dot + gap
        FontMetrics m;
        synchronized (MEASURE) {
            m = MEASURE.getFontMetrics(GraphTheme.bold(10 * ui));
        }
        double right = live ? resetButton(w).getX() - 6 * ui : w - 10 * ui;
        double badgeWidth = m.stringWidth(badge) + 10 * ui;
        double badgeX = right - badgeWidth;
        double room = badgeX - titleX - 8 * ui;
        if (controls == null || controls.width <= 0 || controls.height > hh) {
            return new HeaderLayout(titleX, room, null, badgeX, badgeWidth);
        }
        double gap = 10 * ui;
        double minTitle = Math.min(GraphTitle.width(latex, 14 * ui), 90 * ui);   // keep the function readable
        double titleRoom = room - gap - controls.width;
        if (titleRoom < minTitle) {
            return new HeaderLayout(titleX, room, null, badgeX, badgeWidth);
        }
        double titleWidth = Math.min(titleRoom, GraphTitle.width(latex, 14 * ui));
        Rectangle2D slot = new Rectangle2D.Double(titleX + titleWidth + gap, (hh - controls.height) / 2,
                controls.width, controls.height);
        return new HeaderLayout(titleX, titleWidth, slot, badgeX, badgeWidth);
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

    // what goes in the plot area under the header
    interface PlotLayer {
        void paint(Graphics2D g, Rectangle2D plot, float ui);
    }

    // live = draw buttons + grip; scene may be null, then it's built here
    static void paint(Graphics2D g0, GraphState s, double w, double h, OrbitCamera camera, SurfaceScene scene,
                      GraphTrace hover, GraphTrace pinned, boolean live, int hotButton) {
        paint(g0, s, w, h, camera, scene, hover, pinned, live, hotButton, null);
    }

    static String badge(GraphState s) {
        return s.getKind() == GraphState.Kind.SURFACE ? "3D" : "2D";
    }

    // controls = size of the live x / y boxes sitting in the header, null for none
    static void paint(Graphics2D g0, GraphState s, double w, double h, OrbitCamera camera, SurfaceScene scene,
                      GraphTrace hover, GraphTrace pinned, boolean live, int hotButton, Dimension controls) {
        boolean surface = s.getKind() == GraphState.Kind.SURFACE;
        // colour dot like the notes app: orange line, ramp for a surface
        Color dotLow = surface ? GraphTheme.surface(0, 1, 255) : GraphTheme.CURVE;
        Color dotHigh = surface ? GraphTheme.surface(1, 1, 255) : null;
        paintCard(g0, w, h, s.getLatex(), badge(s), dotLow, dotHigh, live, hotButton, controls, (g, plot, ui) -> {
            if (s.getKind() == GraphState.Kind.CURVE) {
                CurvePainter.paint(g, s, plot, ui, hover, pinned);
            } else if (surface) {
                SurfacePainter.paint(g, s, scene != null ? scene : SurfaceScene.build(s, camera, plot),
                        ui, hover, pinned);
            }
        });
    }

    // the card itself: rounded white body, plot, header w/ dot + typeset title + badge + buttons, grip
    static void paintCard(Graphics2D g0, double w, double h, String latex, String badge, Color dotLow, Color dotHigh,
                          boolean live, int hotButton, PlotLayer layer) {
        paintCard(g0, w, h, latex, badge, dotLow, dotHigh, live, hotButton, null, layer);
    }

    static void paintCard(Graphics2D g0, double w, double h, String latex, String badge, Color dotLow, Color dotHigh,
                          boolean live, int hotButton, Dimension controls, PlotLayer layer) {
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
            layer.paint(g, plotArea(w, h), ui);
            header(g, latex, badge, dotLow, dotHigh, w, ui, live, hotButton, controls);
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

    private static void header(Graphics2D g, String latex, String badge, Color dotLow, Color dotHigh, double w,
                               float ui, boolean live, int hot, Dimension controls) {
        HeaderLayout layout = headerLayout(latex, badge, w, live, controls);
        double hh = headerHeight(w);
        g.setColor(GraphTheme.HEADER);
        g.fill(new Rectangle2D.Double(0, 0, w, hh));
        g.setColor(GraphTheme.HEADER_LINE);
        g.setStroke(new BasicStroke(1f));
        g.draw(new Line2D.Double(0, hh - 0.5, w, hh - 0.5));

        double r = 5 * ui;
        double dotX = 12 * ui;
        Ellipse2D dot = new Ellipse2D.Double(dotX, hh / 2 - r, 2 * r, 2 * r);
        if (dotHigh != null) {
            g.setPaint(new GradientPaint((float) dotX, (float) (hh / 2 + r), dotLow,
                    (float) (dotX + 2 * r), (float) (hh / 2 - r), dotHigh));
        } else {
            g.setColor(dotLow);
        }
        g.fill(dot);

        // badge, then the buttons on the right
        g.setFont(GraphTheme.bold(10 * ui));
        FontMetrics m = g.getFontMetrics();
        double bw = layout.badgeWidth();
        double bh = m.getHeight() + 2 * ui;
        double bx = layout.badgeX();
        g.setColor(GraphTheme.BADGE);
        g.fill(new RoundRectangle2D.Double(bx, hh / 2 - bh / 2, bw, bh, bh, bh));
        g.setColor(GraphTheme.LABEL);
        g.drawString(badge, (float) (bx + 5 * ui), (float) (hh / 2 - bh / 2 + (bh - m.getHeight()) / 2 + m.getAscent()));

        // the live x / y boxes float over layout.controls(); the title stops short of them
        GraphTitle.paint(g, latex, layout.titleX(), hh / 2, layout.titleWidth(), 14 * ui);

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
