package view.graph;

import interface_adapter.graph.GraphState;
import interface_adapter.graph.GraphTick;
import interface_adapter.graph.GraphTrace;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

// draws a 3d graph: floor grid, z edge, shaded mesh far-to-near, tick labels, cursor readout
final class SurfacePainter {
    private static final double LABEL_OUT = 1.16;   // tick labels sit just outside the floor
    private static final double NAME_OUT = 1.42;

    private SurfacePainter() {
    }

    static void paint(Graphics2D g0, GraphState s, SurfaceScene scene, float ui, GraphTrace hover, GraphTrace pinned) {
        Graphics2D g = (Graphics2D) g0.create();
        try {
            g.clip(scene.area());
            double zh = SurfaceScene.Z_HALF;
            // label the floor edges facing us
            double yEdge = nearer(scene, 0, -1, -zh, 0, 1, -zh) ? -1 : 1;
            double xEdge = nearer(scene, -1, 0, -zh, 1, 0, -zh) ? -1 : 1;
            double[] zCorner = leftCorner(scene, zh);
            floor(g, s, scene, ui);
            zEdge(g, scene, zCorner, ui);
            mesh(g, scene, ui);
            labels(g, s, scene, ui, xEdge, yEdge, zCorner);
            if (pinned != null) {
                trace(g, scene, pinned, ui);
            }
            if (hover != null) {
                trace(g, scene, hover, ui);
            }
        } finally {
            g.dispose();
        }
    }

    private static boolean nearer(SurfaceScene sc, double ax, double ay, double az, double bx, double by, double bz) {
        return sc.projectBox(ax, ay, az)[2] >= sc.projectBox(bx, by, bz)[2];
    }

    // floor corner furthest left on screen, z labels go up that edge
    private static double[] leftCorner(SurfaceScene sc, double zh) {
        double[] best = null;
        double bestX = Double.POSITIVE_INFINITY;
        for (double cx : new double[]{-1, 1}) {
            for (double cy : new double[]{-1, 1}) {
                double x = sc.projectBox(cx, cy, -zh)[0];
                if (x < bestX) {
                    bestX = x;
                    best = new double[]{cx, cy};
                }
            }
        }
        return best;
    }

    private static void floor(Graphics2D g, GraphState s, SurfaceScene sc, float ui) {
        double zh = SurfaceScene.Z_HALF;
        Path2D.Double floor = new Path2D.Double();
        double[][] corners = {{-1, -1}, {1, -1}, {1, 1}, {-1, 1}};
        for (int k = 0; k < 4; k++) {
            double[] p = sc.projectBox(corners[k][0], corners[k][1], -zh);
            if (k == 0) {
                floor.moveTo(p[0], p[1]);
            } else {
                floor.lineTo(p[0], p[1]);
            }
        }
        floor.closePath();
        g.setColor(GraphTheme.FLOOR);
        g.fill(floor);
        g.setStroke(new BasicStroke(0.8f * ui));
        g.setColor(GraphTheme.GRID_MAJOR);
        for (GraphTick t : s.getXTicks()) {
            line(g, sc.projectBox(sc.nx(t.getValue()), -1, -zh), sc.projectBox(sc.nx(t.getValue()), 1, -zh));
        }
        for (GraphTick t : s.getYTicks()) {
            line(g, sc.projectBox(-1, sc.ny(t.getValue()), -zh), sc.projectBox(1, sc.ny(t.getValue()), -zh));
        }
        g.setColor(GraphTheme.AXIS);
        g.setStroke(new BasicStroke(1f * ui));
        g.draw(floor);
    }

    private static void zEdge(Graphics2D g, SurfaceScene sc, double[] corner, float ui) {
        double zh = SurfaceScene.Z_HALF;
        g.setColor(GraphTheme.AXIS);
        g.setStroke(new BasicStroke(1f * ui));
        line(g, sc.projectBox(corner[0], corner[1], -zh), sc.projectBox(corner[0], corner[1], zh));
    }

    private static void mesh(Graphics2D g, SurfaceScene sc, float ui) {
        BasicStroke edge = new BasicStroke(0.55f * ui);
        g.setStroke(edge);
        for (SurfaceScene.Quad q : sc.quads()) {
            g.setColor(GraphTheme.surface(q.level, q.light, 255));
            g.fill(q.shape);
            g.setColor(new Color(20, 30, 60, 55));
            g.draw(q.shape);
        }
    }

    private static void labels(Graphics2D g, GraphState s, SurfaceScene sc, float ui,
                               double xEdge, double yEdge, double[] zCorner) {
        double zh = SurfaceScene.Z_HALF;
        g.setFont(GraphTheme.font(10 * ui));
        g.setColor(GraphTheme.LABEL);
        FontMetrics m = g.getFontMetrics();
        for (GraphTick t : s.getXTicks()) {
            centred(g, m, t.getLabel(), sc.projectBox(sc.nx(t.getValue()), yEdge * LABEL_OUT, -zh));
        }
        for (GraphTick t : s.getYTicks()) {
            centred(g, m, t.getLabel(), sc.projectBox(xEdge * LABEL_OUT, sc.ny(t.getValue()), -zh));
        }
        for (GraphTick t : s.getZTicks()) {
            double[] p = sc.projectBox(zCorner[0], zCorner[1], sc.nz(t.getValue()));
            g.drawString(t.getLabel(), (float) (p[0] - m.stringWidth(t.getLabel()) - 6 * ui),
                    (float) (p[1] + m.getAscent() / 2.0 - 1));
        }
        g.setFont(GraphTheme.bold(12 * ui));
        g.setColor(GraphTheme.TITLE);
        FontMetrics b = g.getFontMetrics();
        centred(g, b, "x", sc.projectBox(0, yEdge * NAME_OUT, -zh));
        centred(g, b, "y", sc.projectBox(xEdge * NAME_OUT, 0, -zh));
        double[] top = sc.projectBox(zCorner[0], zCorner[1], zh);
        centred(g, b, "z", new double[]{top[0], top[1] - 10 * ui});
    }

    private static void centred(Graphics2D g, FontMetrics m, String text, double[] p) {
        g.drawString(text, (float) (p[0] - m.stringWidth(text) / 2.0), (float) (p[1] + m.getAscent() / 2.0 - 1));
    }

    // dot on the surface, dashed drop to the floor, bubble with x y z
    private static void trace(Graphics2D g, SurfaceScene sc, GraphTrace t, float ui) {
        double[] p = sc.project(t.getX(), t.getY(), t.getZ());
        double[] base = sc.projectBox(sc.nx(t.getX()), sc.ny(t.getY()), -SurfaceScene.Z_HALF);
        g.setColor(GraphTheme.GUIDE);
        g.setStroke(new BasicStroke(1.1f * ui, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10,
                new float[]{4 * ui, 4 * ui}, 0));
        line(g, base, p);
        double rb = 2.5 * ui;
        g.fill(new Ellipse2D.Double(base[0] - rb, base[1] - rb, 2 * rb, 2 * rb));
        double r = 5.5 * ui;
        g.setColor(GraphTheme.CARD);
        g.fill(new Ellipse2D.Double(p[0] - r - 2 * ui, p[1] - r - 2 * ui, 2 * r + 4 * ui, 2 * r + 4 * ui));
        g.setColor(GraphTheme.TITLE);
        g.fill(new Ellipse2D.Double(p[0] - r, p[1] - r, 2 * r, 2 * r));
        Rectangle2D a = sc.area();
        TraceBubble.draw(g, p[0], p[1], t.getCaption(), t.getText(), ui, a);
    }

    private static void line(Graphics2D g, double[] a, double[] b) {
        g.draw(new Line2D.Double(a[0], a[1], b[0], b[1]));
    }
}
