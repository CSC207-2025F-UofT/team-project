package view.graph;

import interface_adapter.graph.GraphState;

import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// the surface cut into projected quads, far ones first (painter's order), also maps a pixel back to x, y
final class SurfaceScene {
    static final double Z_HALF = 0.72;   // box is a bit squat, looks better
    private static final double[] LIGHT = normalize(new double[]{-0.45, -0.55, 0.75});

    private final GraphState state;
    private final OrbitCamera camera;
    private final Rectangle2D area;
    private final List<Quad> quads = new ArrayList<>();

    static final class Quad {
        final double[] sx = new double[4];
        final double[] sy = new double[4];
        final double[] wx = new double[4];
        final double[] wy = new double[4];
        double depth;
        double level;    // 0 bottom of the z range .. 1 top
        double light;
        Path2D.Double shape;
    }

    private SurfaceScene(GraphState state, OrbitCamera camera, Rectangle2D area) {
        this.state = state;
        this.camera = camera;
        this.area = area;
    }

    static SurfaceScene build(GraphState state, OrbitCamera camera, Rectangle2D area) {
        SurfaceScene scene = new SurfaceScene(state, camera, area);
        scene.cut();
        return scene;
    }

    List<Quad> quads() {
        return quads;
    }

    Rectangle2D area() {
        return area;
    }

    // world point -> screen {x, y, depth}, z clamped into the box
    double[] project(double x, double y, double z) {
        return camera.project(nx(x), ny(y), nz(z), area);
    }

    // box coords straight to screen, for the frame + labels
    double[] projectBox(double bx, double by, double bz) {
        return camera.project(bx, by, bz, area);
    }

    double nx(double x) {
        return -1 + 2 * (x - state.getXMin()) / (state.getXMax() - state.getXMin());
    }

    double ny(double y) {
        return -1 + 2 * (y - state.getYMin()) / (state.getYMax() - state.getYMin());
    }

    double nz(double z) {
        double zc = Math.max(state.getZMin(), Math.min(state.getZMax(), z));
        return (-1 + 2 * (zc - state.getZMin()) / (state.getZMax() - state.getZMin())) * Z_HALF;
    }

    private void cut() {
        double[][] z = state.getZ();
        if (z == null || z.length < 2 || z[0].length < 2) {
            return;
        }
        int n = z.length;
        int m = z[0].length;
        int[][] corner = {{0, 0}, {1, 0}, {1, 1}, {0, 1}};
        double[][] box = new double[4][3];
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < m - 1; j++) {
                Quad q = new Quad();
                boolean hole = false;
                double zSum = 0;
                for (int k = 0; k < 4 && !hole; k++) {
                    int a = i + corner[k][0];
                    int b = j + corner[k][1];
                    double v = z[a][b];
                    if (Double.isNaN(v)) {
                        hole = true;
                        break;
                    }
                    q.wx[k] = state.getXMin() + (state.getXMax() - state.getXMin()) * a / (n - 1);
                    q.wy[k] = state.getYMin() + (state.getYMax() - state.getYMin()) * b / (m - 1);
                    box[k][0] = -1 + 2.0 * a / (n - 1);
                    box[k][1] = -1 + 2.0 * b / (m - 1);
                    box[k][2] = nz(v);
                    double[] p = camera.project(box[k][0], box[k][1], box[k][2], area);
                    q.sx[k] = p[0];
                    q.sy[k] = p[1];
                    q.depth += p[2] / 4;
                    zSum += box[k][2];
                }
                if (hole) {
                    continue;
                }
                q.level = (zSum / 4 / Z_HALF + 1) / 2;
                double[] normal = normalize(cross(sub(box[2], box[0]), sub(box[3], box[1])));
                q.light = 0.55 + 0.45 * Math.abs(dot(normal, LIGHT));
                q.shape = new Path2D.Double();
                q.shape.moveTo(q.sx[0], q.sy[0]);
                for (int k = 1; k < 4; k++) {
                    q.shape.lineTo(q.sx[k], q.sy[k]);
                }
                q.shape.closePath();
                quads.add(q);
            }
        }
        quads.sort(Comparator.comparingDouble(q -> q.depth));
    }

    // pixel -> {x, y} on the nearest quad under it, null if it's off the surface
    double[] pick(double px, double py) {
        for (int k = quads.size() - 1; k >= 0; k--) {
            Quad q = quads.get(k);
            if (!q.shape.contains(px, py)) {
                continue;
            }
            double[] hit = inTriangle(q, 0, 1, 2, px, py);
            if (hit == null) {
                hit = inTriangle(q, 0, 2, 3, px, py);
            }
            if (hit == null) {
                hit = new double[]{(q.wx[0] + q.wx[2]) / 2, (q.wy[0] + q.wy[2]) / 2};
            }
            return hit;
        }
        return null;
    }

    // barycentric blend of the world corners if (px, py) is inside triangle a b c
    private static double[] inTriangle(Quad q, int a, int b, int c, double px, double py) {
        double det = (q.sy[b] - q.sy[c]) * (q.sx[a] - q.sx[c]) + (q.sx[c] - q.sx[b]) * (q.sy[a] - q.sy[c]);
        if (Math.abs(det) < 1e-12) {
            return null;
        }
        double l1 = ((q.sy[b] - q.sy[c]) * (px - q.sx[c]) + (q.sx[c] - q.sx[b]) * (py - q.sy[c])) / det;
        double l2 = ((q.sy[c] - q.sy[a]) * (px - q.sx[c]) + (q.sx[a] - q.sx[c]) * (py - q.sy[c])) / det;
        double l3 = 1 - l1 - l2;
        double eps = -1e-6;
        if (l1 < eps || l2 < eps || l3 < eps) {
            return null;
        }
        return new double[]{l1 * q.wx[a] + l2 * q.wx[b] + l3 * q.wx[c], l1 * q.wy[a] + l2 * q.wy[b] + l3 * q.wy[c]};
    }

    private static double[] sub(double[] a, double[] b) {
        return new double[]{a[0] - b[0], a[1] - b[1], a[2] - b[2]};
    }

    private static double[] cross(double[] a, double[] b) {
        return new double[]{a[1] * b[2] - a[2] * b[1], a[2] * b[0] - a[0] * b[2], a[0] * b[1] - a[1] * b[0]};
    }

    private static double dot(double[] a, double[] b) {
        return a[0] * b[0] + a[1] * b[1] + a[2] * b[2];
    }

    private static double[] normalize(double[] v) {
        double len = Math.sqrt(dot(v, v));
        return len == 0 ? new double[]{0, 0, 1} : new double[]{v[0] / len, v[1] / len, v[2] / len};
    }
}
