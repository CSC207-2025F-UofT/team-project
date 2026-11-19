package view.graph;

import interface_adapter.phase.PhaseMarker;
import interface_adapter.phase.PhaseReadout;
import interface_adapter.phase.PhaseState;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.List;

// draws a phase portrait: grid, axes w/ names, arrows (or slope dashes), equilibria, pinned + hovered solutions
final class PhasePainter {
    private PhasePainter() {
    }

    static void paint(Graphics2D g0, PhaseState s, Rectangle2D area, float ui, PhaseReadout hover) {
        Graphics2D g = (Graphics2D) g0.create();
        try {
            g.clip(area);
            PlotFrame f = new PlotFrame(s.getXMin(), s.getXMax(), s.getYMin(), s.getYMax(), area);
            AxesPainter.paint(g, f, s.getXTicks(), s.getYTicks(), ui);
            axisNames(g, s, f, ui);
            field(g, s, f, ui);
            lines(g, s, f, ui);
            for (double[][] piece : s.getSolutions()) {
                path(g, f, piece, GraphTheme.CURVE, 2.4f * ui, ui, s.getKind() == PhaseState.Kind.PHASE_PLANE);
            }
            double dotY = 0;
            if (hover != null) {
                Color soft = new Color(GraphTheme.CURVE.getRed(), GraphTheme.CURVE.getGreen(),
                        GraphTheme.CURVE.getBlue(), 150);
                for (double[][] piece : hover.getPath()) {
                    path(g, f, piece, soft, 1.8f * ui, ui, true);
                }
                dotY = AxesPainter.guides(g, f, f.px(hover.getH()), f.py(hover.getV()), hover.getHText(),
                        hover.getVText(), ui);
            }
            dots(g, s, f, ui);
            if (hover != null) {
                double px = f.px(hover.getH());
                double py = f.py(hover.getV());
                if (area.contains(px, py)) {
                    double r = 5.5 * ui;
                    g.setColor(GraphTheme.CARD);
                    g.fill(new Ellipse2D.Double(px - r - 2 * ui, py - r - 2 * ui, 2 * r + 4 * ui, 2 * r + 4 * ui));
                    g.setColor(GraphTheme.CURVE);
                    g.fill(new Ellipse2D.Double(px - r, py - r, 2 * r, 2 * r));
                }
                TraceBubble.draw(g, px, dotY, hover.getCaption(), hover.getText(), hover.getDetail(), ui, area);
            }
        } finally {
            g.dispose();
        }
    }

    // "x" at the right end of the across axis, "y" at the top of the up one
    private static void axisNames(Graphics2D g, PhaseState s, PlotFrame f, float ui) {
        Rectangle2D a = f.area();
        g.setFont(GraphTheme.italic(13 * ui));
        g.setColor(GraphTheme.TITLE);
        FontMetrics m = g.getFontMetrics();
        double pad = 6 * ui;
        double axisY = TraceBubble.clamp(f.py(0), a.getMinY() + m.getHeight() + pad, a.getMaxY() - pad);
        double axisX = TraceBubble.clamp(f.px(0), a.getMinX() + pad, a.getMaxX() - 40 * ui);
        String h = s.getHorizontalLabel();
        g.drawString(h, (float) (a.getMaxX() - pad - m.stringWidth(h)), (float) (axisY - pad));
        g.drawString(s.getVerticalLabel(), (float) (axisX + pad), (float) (a.getMinY() + pad + m.getAscent()));
    }

    // arrows sized + coloured by speed on a phase plane, plain dashes on a slope field
    private static void field(Graphics2D g, PhaseState s, PlotFrame f, float ui) {
        double[][] a = s.getArrows();
        int n = a[0].length;
        if (n == 0) {
            return;
        }
        Rectangle2D area = f.area();
        double cell = Math.sqrt(area.getWidth() * area.getHeight() / n);
        double[] sx = new double[n];
        double[] sy = new double[n];
        double[] speed = new double[n];
        for (int i = 0; i < n; i++) {
            sx[i] = a[2][i] / f.xPerPixel();    // world -> screen, so slopes look right on a squashed window
            sy[i] = -a[3][i] / f.yPerPixel();
            speed[i] = Math.hypot(sx[i], sy[i]);
        }
        boolean slope = s.getKind() == PhaseState.Kind.SLOPE_FIELD;
        double ref = slope ? 1 : typical(speed);
        g.setStroke(new BasicStroke((slope ? 1.4f : 1.3f) * ui, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        for (int i = 0; i < n; i++) {
            if (!Double.isFinite(speed[i]) || speed[i] == 0) {
                continue;
            }
            double ux = sx[i] / speed[i];
            double uy = sy[i] / speed[i];
            double cx = f.px(a[0][i]);
            double cy = f.py(a[1][i]);
            if (slope) {
                double half = 0.32 * cell;
                g.setColor(GraphTheme.SLOPE);
                g.draw(new Line2D.Double(cx - ux * half, cy - uy * half, cx + ux * half, cy + uy * half));
                continue;
            }
            double t = Math.min(1, speed[i] / ref);
            double half = cell * (0.16 + 0.22 * Math.sqrt(t));
            g.setColor(GraphTheme.speed(t, 215));
            double tipX = cx + ux * half;
            double tipY = cy + uy * half;
            g.draw(new Line2D.Double(cx - ux * half, cy - uy * half, tipX, tipY));
            head(g, tipX, tipY, ux, uy, 3.4 * ui);
        }
    }

    // 80th percentile speed, so one huge arrow doesn't shrink the rest to dots
    private static double typical(double[] speed) {
        double[] finite = Arrays.stream(speed).filter(v -> Double.isFinite(v) && v > 0).sorted().toArray();
        return finite.length == 0 ? 1 : finite[(int) Math.floor(0.8 * (finite.length - 1))];
    }

    private static void head(Graphics2D g, double x, double y, double ux, double uy, double size) {
        Path2D.Double p = new Path2D.Double();
        p.moveTo(x, y);
        p.lineTo(x - ux * size * 1.6 - uy * size, y - uy * size * 1.6 + ux * size);
        p.lineTo(x - ux * size * 1.6 + uy * size, y - uy * size * 1.6 - ux * size);
        p.closePath();
        g.fill(p);
    }

    // equilibrium solutions on a slope field: dashed v = c right across
    private static void lines(Graphics2D g, PhaseState s, PlotFrame f, float ui) {
        Rectangle2D a = f.area();
        g.setStroke(new BasicStroke(1.8f * ui, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10,
                new float[]{7 * ui, 5 * ui}, 0));
        for (PhaseMarker m : s.getMarkers()) {
            if (m.isLine()) {
                g.setColor(colour(m.getRole()));
                g.draw(new Line2D.Double(a.getMinX(), f.py(m.getV()), a.getMaxX(), f.py(m.getV())));
            }
        }
    }

    // filled = pulls in, ring = pushes out / saddle / goes round
    private static void dots(Graphics2D g, PhaseState s, PlotFrame f, float ui) {
        double r = 4.6 * ui;
        g.setStroke(new BasicStroke(2f * ui));
        for (PhaseMarker m : s.getMarkers()) {
            if (m.isLine()) {
                continue;
            }
            double x = f.px(m.getH());
            double y = f.py(m.getV());
            Ellipse2D dot = new Ellipse2D.Double(x - r, y - r, 2 * r, 2 * r);
            Color c = colour(m.getRole());
            g.setColor(m.isStable() ? c : GraphTheme.CARD);
            g.fill(dot);
            g.setColor(c);
            g.draw(dot);
            if (m.getRole() == PhaseMarker.Role.SADDLE || m.getRole() == PhaseMarker.Role.CENTER) {
                double k = r * 0.42;
                g.fill(new Ellipse2D.Double(x - k, y - k, 2 * k, 2 * k));
            }
        }
    }

    static Color colour(PhaseMarker.Role role) {
        switch (role) {
            case ATTRACTOR:
                return GraphTheme.ATTRACTOR;
            case REPELLER:
                return GraphTheme.REPELLER;
            case SADDLE:
                return GraphTheme.SADDLE;
            case CENTER:
                return GraphTheme.CENTER;
            default:
                return GraphTheme.NEUTRAL;
        }
    }

    // a solution curve; arrows partway along show which way it runs
    private static void path(Graphics2D g, PlotFrame f, double[][] piece, Color c, float width, float ui,
                             boolean arrows) {
        double[] xs = piece[0];
        double[] ys = piece[1];
        if (xs.length < 2) {
            return;
        }
        Path2D.Double p = new Path2D.Double();
        p.moveTo(f.px(xs[0]), f.py(ys[0]));
        for (int i = 1; i < xs.length; i++) {
            p.lineTo(f.px(xs[i]), f.py(ys[i]));
        }
        g.setColor(c);
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.draw(p);
        if (arrows) {
            arrowsAlong(g, f, xs, ys, 4.2 * ui);
        }
    }

    // heads at a third + two thirds of the part you can see
    private static void arrowsAlong(Graphics2D g, PlotFrame f, double[] xs, double[] ys, double size) {
        Rectangle2D a = f.area();
        double[] along = new double[xs.length];
        double total = 0;
        for (int i = 1; i < xs.length; i++) {
            double x0 = f.px(xs[i - 1]);
            double y0 = f.py(ys[i - 1]);
            double x1 = f.px(xs[i]);
            double y1 = f.py(ys[i]);
            if (a.contains(x1, y1)) {
                total += Math.hypot(x1 - x0, y1 - y0);
            }
            along[i] = total;
        }
        if (total < 40) {
            return;
        }
        List<Double> at = total < 160 ? List.of(0.5) : List.of(1 / 3.0, 2 / 3.0);
        for (double share : at) {
            double want = share * total;
            for (int i = 1; i < xs.length; i++) {
                if (along[i] >= want) {
                    double x0 = f.px(xs[i - 1]);
                    double y0 = f.py(ys[i - 1]);
                    double x1 = f.px(xs[i]);
                    double y1 = f.py(ys[i]);
                    double len = Math.hypot(x1 - x0, y1 - y0);
                    if (len > 0) {
                        head(g, x1, y1, (x1 - x0) / len, (y1 - y0) / len, size);
                    }
                    break;
                }
            }
        }
    }
}
