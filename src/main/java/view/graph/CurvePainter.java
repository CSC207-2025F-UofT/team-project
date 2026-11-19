package view.graph;

import interface_adapter.graph.GraphMarker;
import interface_adapter.graph.GraphState;
import interface_adapter.graph.GraphTrace;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

// draws a 2d graph: grid, axes, labels, the curve, key point dots, the cursor readout
final class CurvePainter {
    private CurvePainter() {
    }

    static void paint(Graphics2D g0, GraphState s, Rectangle2D area, float ui, GraphTrace hover, GraphTrace pinned) {
        Graphics2D g = (Graphics2D) g0.create();
        try {
            g.clip(area);
            PlotFrame f = new PlotFrame(s, area);
            AxesPainter.paint(g, f, s.getXTicks(), s.getYTicks(), ui);
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
        double dotY = AxesPainter.guides(g, f, px, py, t.getXText(), t.getYText(), ui);
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
}
