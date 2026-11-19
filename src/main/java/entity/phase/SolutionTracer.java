package entity.phase;

import entity.graph.Curve;
import entity.graph.Viewport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Follows the field both ways from a point (RK4). Steps are a fixed bit of the window, not of time,
 * so slow and fast parts draw equally smooth and stiff spots don't blow up; it's the path we want.
 */
public final class SolutionTracer {
    static final double STEP = 1.0 / 320;    // of the window per step
    static final int MAX_STEPS = 2600;        // each way
    private static final double MARGIN = 0.2; // keep going a bit past the edge so pans don't show the cut

    // the path through (h, v); empty if it's sitting on an equilibrium
    public Curve through(DifferentialEquation eq, Viewport view, double h, double v) {
        List<double[]> back = run(eq, view, h, v, -1);
        List<double[]> ahead = run(eq, view, h, v, 1);
        Collections.reverse(back);
        List<double[]> all = new ArrayList<>(back);
        if (!ahead.isEmpty() && !all.isEmpty()) {
            ahead = ahead.subList(1, ahead.size());   // start's in both
        }
        all.addAll(ahead);
        if (all.size() < 2) {
            return new Curve(List.of());
        }
        double[] xs = new double[all.size()];
        double[] ys = new double[all.size()];
        for (int i = 0; i < all.size(); i++) {
            xs[i] = all.get(i)[0];
            ys[i] = all.get(i)[1];
        }
        return new Curve(List.of(new Curve.Piece(xs, ys)));
    }

    private static List<double[]> run(DifferentialEquation eq, Viewport view, double h0, double v0, int sign) {
        double sx = view.width();
        double sy = view.height();
        double hLo = view.getXMin() - MARGIN * sx;
        double hHi = view.getXMax() + MARGIN * sx;
        double vLo = view.getYMin() - MARGIN * sy;
        double vHi = view.getYMax() + MARGIN * sy;
        List<double[]> pts = new ArrayList<>();
        pts.add(new double[]{h0, v0});
        double h = h0;
        double v = v0;
        double[] before = null;
        int flips = 0;
        double travelled = 0;
        for (int step = 0; step < MAX_STEPS; step++) {
            double[] k1 = direction(eq, h, v, sx, sy, sign);
            double[] k2 = k1 == null ? null : direction(eq, h + STEP / 2 * k1[0] * sx, v + STEP / 2 * k1[1] * sy, sx, sy, sign);
            double[] k3 = k2 == null ? null : direction(eq, h + STEP / 2 * k2[0] * sx, v + STEP / 2 * k2[1] * sy, sx, sy, sign);
            double[] k4 = k3 == null ? null : direction(eq, h + STEP * k3[0] * sx, v + STEP * k3[1] * sy, sx, sy, sign);
            if (k4 == null) {
                break;   // undefined or resting
            }
            double nh = (k1[0] + 2 * k2[0] + 2 * k3[0] + k4[0]) / 6;
            double nv = (k1[1] + 2 * k2[1] + 2 * k3[1] + k4[1]) / 6;
            // turning back on itself = circling a sink smaller than a step, call it done
            if (before != null && before[0] * nh + before[1] * nv < -0.3 && ++flips >= 3) {
                break;
            }
            before = new double[]{nh, nv};
            h += STEP * nh * sx;
            v += STEP * nv * sy;
            if (!Double.isFinite(h) || !Double.isFinite(v)) {
                break;
            }
            pts.add(new double[]{h, v});
            travelled += STEP;
            if (h < hLo || h > hHi || v < vLo || v > vHi) {
                break;
            }
            // came back round to the start: closed orbit, shut it and stop
            if (travelled > 20 * STEP && Math.hypot((h - h0) / sx, (v - v0) / sy) < 0.8 * STEP) {
                pts.add(new double[]{h0, v0});
                break;
            }
        }
        return pts;
    }

    // unit direction in window-scaled coords, null if undefined or not moving
    private static double[] direction(DifferentialEquation eq, double h, double v, double sx, double sy, int sign) {
        double a = eq.dh(h, v) / sx;
        double b = eq.dv(h, v) / sy;
        double n = Math.hypot(a, b);
        if (!Double.isFinite(n) || n < 1e-12) {
            return null;
        }
        return new double[]{sign * a / n, sign * b / n};
    }
}
