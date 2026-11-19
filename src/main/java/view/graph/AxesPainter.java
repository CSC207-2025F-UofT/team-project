package view.graph;

import interface_adapter.graph.GraphTick;

import java.awt.BasicStroke;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

// grid, axes + tick labels for any flat plot, and the dashed guides from a cursor point back to the axes
final class AxesPainter {
    private AxesPainter() {
    }

    static void paint(Graphics2D g, PlotFrame f, List<GraphTick> xTicks, List<GraphTick> yTicks, float ui) {
        grid(g, f, xTicks, yTicks, ui);
        axes(g, f, ui);
        labels(g, f, xTicks, yTicks, ui);
    }

    // dashed lines from (px, py) to both axes + value pills there when there's room; gives back the dot's y on screen
    static double guides(Graphics2D g, PlotFrame f, double px, double py, String xText, String yText, float ui) {
        Rectangle2D a = f.area();
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
            TraceBubble.pill(g, px, axisY + 11 * ui, xText, ui, a);
        }
        if (Math.abs(px - axisX) > 44 * ui) {
            TraceBubble.pill(g, axisX, dotY, yText, ui, a);
        }
        return dotY;
    }

    private static void grid(Graphics2D g, PlotFrame f, List<GraphTick> xTicks, List<GraphTick> yTicks, float ui) {
        Rectangle2D a = f.area();
        g.setStroke(new BasicStroke(Math.max(0.6f, 0.8f * ui)));
        double xStep = step(xTicks);
        double yStep = step(yTicks);
        // minor lines, only if they're not crammed together
        g.setColor(GraphTheme.GRID_MINOR);
        if (xStep > 0 && xStep / 5 / f.xPerPixel() >= 7) {
            for (double x = Math.ceil(f.xMin() / (xStep / 5)) * (xStep / 5); x <= f.xMax(); x += xStep / 5) {
                g.draw(new Line2D.Double(f.px(x), a.getMinY(), f.px(x), a.getMaxY()));
            }
        }
        if (yStep > 0 && yStep / 5 / f.yPerPixel() >= 7) {
            for (double y = Math.ceil(f.yMin() / (yStep / 5)) * (yStep / 5); y <= f.yMax(); y += yStep / 5) {
                g.draw(new Line2D.Double(a.getMinX(), f.py(y), a.getMaxX(), f.py(y)));
            }
        }
        g.setColor(GraphTheme.GRID_MAJOR);
        for (GraphTick t : xTicks) {
            g.draw(new Line2D.Double(f.px(t.getValue()), a.getMinY(), f.px(t.getValue()), a.getMaxY()));
        }
        for (GraphTick t : yTicks) {
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
    private static void labels(Graphics2D g, PlotFrame f, List<GraphTick> xTicks, List<GraphTick> yTicks, float ui) {
        Rectangle2D a = f.area();
        g.setFont(GraphTheme.font(10.5f * ui));
        g.setColor(GraphTheme.LABEL);
        FontMetrics m = g.getFontMetrics();
        double pad = 4 * ui;
        double xRow = TraceBubble.clamp(f.py(0) + pad, a.getMinY() + pad, a.getMaxY() - m.getHeight() - pad);
        int every = thin(xTicks, f.xPerPixel(), m, 12 * ui);
        List<GraphTick> xs = xTicks;
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
        for (GraphTick t : yTicks) {
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

    private static double step(List<GraphTick> ticks) {
        return ticks.size() < 2 ? 0 : ticks.get(1).getValue() - ticks.get(0).getValue();
    }
}
