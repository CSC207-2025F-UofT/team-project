package view.graph;

import interface_adapter.graph.GraphMarker;
import interface_adapter.graph.GraphState;
import interface_adapter.graph.GraphTick;
import interface_adapter.graph.GraphTrace;

import java.awt.BasicStroke;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

// draws a 2d graph: grid, axes, labels, the curve, key point dots, the cursor readout
final class CurvePainter {
    private CurvePainter() {
    }

    static void paint(Graphics2D g0, GraphState s, Rectangle2D area, float ui, GraphTrace hover, GraphTrace pinned) {
        Graphics2D g = (Graphics2D) g0.create();
        try {
            g.clip(area);
            PlotFrame f = new PlotFrame(s, area);
            grid(g, s, f, ui);
            axes(g, f, ui);
            labels(g, s, f, ui);
            curve(g, s, f, ui);
            markers(g, s, f, ui);
            if (pinned != null) {
                trace(g, f, pinned, ui);
            }
            if (hover != null) {
                trace(g, f, hover, ui);
            }
        } finally {
            g.dispose();
        }
    }

    private static void grid(Graphics2D g, GraphState s, PlotFrame f, float ui) {
        Rectangle2D a = f.area();
        g.setStroke(new BasicStroke(Math.max(0.6f, 0.8f * ui)));
        double xStep = step(s.getXTicks());
        double yStep = step(s.getYTicks());
        // minor lines, only if they're not crammed together
        g.setColor(GraphTheme.GRID_MINOR);
        if (xStep > 0 && xStep / 5 / f.xPerPixel() >= 7) {
            for (double x = Math.ceil(s.getXMin() / (xStep / 5)) * (xStep / 5); x <= s.getXMax(); x += xStep / 5) {
                g.draw(new Line2D.Double(f.px(x), a.getMinY(), f.px(x), a.getMaxY()));
            }
        }
        if (yStep > 0 && yStep / 5 / f.yPerPixel() >= 7) {
            for (double y = Math.ceil(s.getYMin() / (yStep / 5)) * (yStep / 5); y <= s.getYMax(); y += yStep / 5) {
                g.draw(new Line2D.Double(a.getMinX(), f.py(y), a.getMaxX(), f.py(y)));
            }
        }
        g.setColor(GraphTheme.GRID_MAJOR);
        for (GraphTick t : s.getXTicks()) {
            g.draw(new Line2D.Double(f.px(t.getValue()), a.getMinY(), f.px(t.getValue()), a.getMaxY()));
        }
        for (GraphTick t : s.getYTicks()) {
            g.draw(new Line2D.Double(a.getMinX(), f.py(t.getValue()), a.getMaxX(), f.py(t.getValue())));
        }
    }

    private static void axes(Graphics2D g, PlotFrame f, float ui) {
        Rectangle2D a = f.area();
        g.setColor(GraphTheme.AXIS);
        g.setStroke(new BasicStroke(1.2f * ui));
        double y0 = f.py(0);
        if (y0 >= a.getMinY() && y0 <= a.getMaxY()) {
            g.draw(new Line2D.Double(a.getMinX(), y0, a.getMaxX(), y0));
        }
        double x0 = f.px(0);
        if (x0 >= a.getMinX() && x0 <= a.getMaxX()) {
            g.draw(new Line2D.Double(x0, a.getMinY(), x0, a.getMaxY()));
        }
    }

    // labels hug the axes, slide to the edge when the axis is off screen
    private static void labels(Graphics2D g, GraphState s, PlotFrame f, float ui) {
        Rectangle2D a = f.area();
        g.setFont(GraphTheme.font(10.5f * ui));
        g.setColor(GraphTheme.LABEL);
        FontMetrics m = g.getFontMetrics();
        double pad = 4 * ui;
        double xRow = TraceBubble.clamp(f.py(0) + pad, a.getMinY() + pad, a.getMaxY() - m.getHeight() - pad);
        int every = thin(s.getXTicks(), f.xPerPixel(), m, 12 * ui);
        List<GraphTick> xs = s.getXTicks();
        for (int i = 0; i < xs.size(); i++) {
            GraphTick t = xs.get(i);
            if (i % every != 0 || "0".equals(t.getLabel())) {
                continue;
            }
            double cx = f.px(t.getValue()) - m.stringWidth(t.getLabel()) / 2.0;
            if (cx < a.getMinX() + 2 || cx + m.stringWidth(t.getLabel()) > a.getMaxX() - 2) {
                continue;   // half cut off at the edge looks broken
            }
            g.drawString(t.getLabel(), (float) cx, (float) (xRow + m.getAscent()));
        }
        double yCol = f.px(0) + pad;
        for (GraphTick t : s.getYTicks()) {
            if ("0".equals(t.getLabel())) {
                continue;
            }
            double w = m.stringWidth(t.getLabel());
            double x = TraceBubble.clamp(yCol, a.getMinX() + pad, a.getMaxX() - w - pad);
            double y = f.py(t.getValue()) + m.getAscent() / 2.0 - 1;
            if (y - m.getAscent() > a.getMinY() && y < a.getMaxY()) {
                g.drawString(t.getLabel(), (float) x, (float) y);
            }
        }
        // one 0 for the origin
        if (a.contains(f.px(0), f.py(0))) {
            g.drawString("0", (float) (f.px(0) + pad), (float) (f.py(0) + pad + m.getAscent()));
        }
    }

    // draw every n-th label so wide ones don't overlap
    private static int thin(List<GraphTick> ticks, double xPerPixel, FontMetrics m, double gap) {
        if (ticks.size() < 2) {
            return 1;
        }
        double widest = 0;
        for (GraphTick t : ticks) {
            widest = Math.max(widest, m.stringWidth(t.getLabel()));
        }
        double px = (ticks.get(1).getValue() - ticks.get(0).getValue()) / xPerPixel;
        return Math.max(1, (int) Math.ceil((widest + gap) / px));
    }

    private static void curve(Graphics2D g, GraphState s, PlotFrame f, float ui) {
        g.setColor(GraphTheme.CURVE);
        g.setStroke(new BasicStroke(2.6f * ui, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        for (double[][] piece : s.getPieces()) {
            double[] xs = piece[0];
            double[] ys = piece[1];
            if (xs.length == 1) {
                double r = 2 * ui;
                g.fill(new Ellipse2D.Double(f.px(xs[0]) - r, f.py(ys[0]) - r, 2 * r, 2 * r));
                continue;
            }
            Path2D.Double path = new Path2D.Double();
            path.moveTo(f.px(xs[0]), f.py(ys[0]));
            for (int i = 1; i < xs.length; i++) {
                path.lineTo(f.px(xs[i]), f.py(ys[i]));
            }
            g.draw(path);
        }
    }

    private static void markers(Graphics2D g, GraphState s, PlotFrame f, float ui) {
        double r = 3.4 * ui;
        g.setStroke(new BasicStroke(1.6f * ui));
        for (GraphMarker k : s.getMarkers()) {
            Ellipse2D dot = new Ellipse2D.Double(f.px(k.getX()) - r, f.py(k.getY()) - r, 2 * r, 2 * r);
            g.setColor(GraphTheme.CARD);
            g.fill(dot);
            g.setColor(GraphTheme.CURVE);
            g.draw(dot);
        }
    }

    // dot on the curve, dashed lines back to both axes, values pinned on the axes, bubble
    private static void trace(Graphics2D g, PlotFrame f, GraphTrace t, float ui) {
        Rectangle2D a = f.area();
        double px = f.px(t.getX());
        double py = f.py(t.getY());
        double axisY = TraceBubble.clamp(f.py(0), a.getMinY(), a.getMaxY());
        double axisX = TraceBubble.clamp(f.px(0), a.getMinX(), a.getMaxX());
        double dotY = TraceBubble.clamp(py, a.getMinY(), a.getMaxY());
        g.setColor(GraphTheme.GUIDE);
        g.setStroke(new BasicStroke(1.1f * ui, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10,
                new float[]{4 * ui, 4 * ui}, 0));
        g.draw(new Line2D.Double(px, dotY, px, axisY));
        g.draw(new Line2D.Double(px, dotY, axisX, dotY));
        // axis tags only when there's room, right on top of the dot the bubble says it anyway
        if (Math.abs(dotY - axisY) > 26 * ui) {
            TraceBubble.pill(g, px, axisY + 11 * ui, t.getXText(), ui, a);
        }
        if (Math.abs(px - axisX) > 44 * ui) {
            TraceBubble.pill(g, axisX, dotY, t.getYText(), ui, a);
        }
        if (a.contains(px, py)) {
            double r = 6 * ui;
            Ellipse2D dot = new Ellipse2D.Double(px - r, py - r, 2 * r, 2 * r);
            g.setColor(GraphTheme.CARD);
            g.fill(new Ellipse2D.Double(px - r - 2 * ui, py - r - 2 * ui, 2 * r + 4 * ui, 2 * r + 4 * ui));
            g.setColor(GraphTheme.CURVE);
            g.fill(dot);
        }
        TraceBubble.draw(g, px, dotY, t.getCaption(), t.getText(), ui, a);
    }

    private static double step(List<GraphTick> ticks) {
        return ticks.size() < 2 ? 0 : ticks.get(1).getValue() - ticks.get(0).getValue();
    }
}
