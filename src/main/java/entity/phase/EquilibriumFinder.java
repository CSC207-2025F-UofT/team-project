package entity.phase;

import entity.graph.Viewport;

import java.util.ArrayList;
import java.util.List;

/**
 * Phase plane: Newton from a grid of seeds, then the Jacobian says saddle / node / spiral / center.
 * Slope field: lines v = c where the slope is 0 for every h in view (y' = y(1 - y) -> y = 0, y = 1).
 */
public final class EquilibriumFinder {
    static final int SEEDS = 12;          // per side
    static final int MAX_POINTS = 24;
    private static final int NEWTON_STEPS = 40;
    private static final int LINE_SCAN = 400;
    private static final int LINE_COLUMNS = 7;

    public List<Equilibrium> find(DifferentialEquation eq, Viewport view) {
        return eq.getKind() == DifferentialEquation.Kind.SLOPE_FIELD ? lines(eq, view) : points(eq, view);
    }

    // ---------------------------------------------------------------- points

    private static List<Equilibrium> points(DifferentialEquation eq, Viewport view) {
        List<Equilibrium> out = new ArrayList<>();
        double sx = view.width();
        double sy = view.height();
        for (int i = 0; i <= SEEDS && out.size() < MAX_POINTS; i++) {
            for (int j = 0; j <= SEEDS && out.size() < MAX_POINTS; j++) {
                double[] p = newton(eq, view.getXMin() + sx * i / SEEDS, view.getYMin() + sy * j / SEEDS, sx, sy);
                if (p == null || !view.containsX(p[0]) || !view.containsY(p[1]) || seen(out, p, sx, sy)) {
                    continue;
                }
                double h = Math.abs(p[0]) / sx < 1e-9 ? 0 : p[0];   // -2.9e-23 is just 0
                double v = Math.abs(p[1]) / sy < 1e-9 ? 0 : p[1];
                out.add(new Equilibrium(h, v, classify(eq, h, v, sx, sy)));
            }
        }
        return out;
    }

    private static boolean seen(List<Equilibrium> found, double[] p, double sx, double sy) {
        for (Equilibrium e : found) {
            if (Math.abs(e.getH() - p[0]) / sx < 1e-4 && Math.abs(e.getV() - p[1]) / sy < 1e-4) {
                return true;
            }
        }
        return false;
    }

    private static double[] newton(DifferentialEquation eq, double h, double v, double sx, double sy) {
        for (int k = 0; k < NEWTON_STEPS; k++) {
            double f = eq.dh(h, v);
            double g = eq.dv(h, v);
            if (!Double.isFinite(f) || !Double.isFinite(g)) {
                return null;
            }
            double[] j = jacobian(eq, h, v, sx, sy);
            double det = j[0] * j[3] - j[1] * j[2];
            if (!Double.isFinite(det) || Math.abs(det) < 1e-14) {
                return null;
            }
            double dh = (f * j[3] - g * j[1]) / det;
            double dv = (g * j[0] - f * j[2]) / det;
            h -= dh;
            v -= dv;
            if (!Double.isFinite(h) || !Double.isFinite(v)) {
                return null;
            }
            if (Math.abs(dh) / sx < 1e-12 && Math.abs(dv) / sy < 1e-12) {
                break;
            }
        }
        double f = eq.dh(h, v);
        double g = eq.dv(h, v);
        double tol = 1e-7 * (1 + Math.abs(h) + Math.abs(v));
        return Math.abs(f) < tol && Math.abs(g) < tol ? new double[]{h, v} : null;
    }

    // central differences, {dF/dh, dF/dv, dG/dh, dG/dv}
    static double[] jacobian(DifferentialEquation eq, double h, double v, double sx, double sy) {
        double eh = 1e-6 * Math.max(sx, Math.abs(h));
        double ev = 1e-6 * Math.max(sy, Math.abs(v));
        return new double[]{
            (eq.dh(h + eh, v) - eq.dh(h - eh, v)) / (2 * eh),
            (eq.dh(h, v + ev) - eq.dh(h, v - ev)) / (2 * ev),
            (eq.dv(h + eh, v) - eq.dv(h - eh, v)) / (2 * eh),
            (eq.dv(h, v + ev) - eq.dv(h, v - ev)) / (2 * ev)};
    }

    // trace / det of the linearisation
    static Equilibrium.Type classify(DifferentialEquation eq, double h, double v, double sx, double sy) {
        double[] j = jacobian(eq, h, v, sx, sy);
        double tr = j[0] + j[3];
        double det = j[0] * j[3] - j[1] * j[2];
        double scale = Math.abs(j[0]) + Math.abs(j[1]) + Math.abs(j[2]) + Math.abs(j[3]);
        if (!Double.isFinite(det) || Math.abs(det) <= 1e-9 * Math.max(1e-12, scale * scale)) {
            return Equilibrium.Type.DEGENERATE;
        }
        if (det < 0) {
            return Equilibrium.Type.SADDLE;
        }
        if (Math.abs(tr) <= 1e-5 * Math.sqrt(det)) {
            return Equilibrium.Type.CENTER;
        }
        boolean spiral = tr * tr - 4 * det < 0;
        if (tr < 0) {
            return spiral ? Equilibrium.Type.STABLE_SPIRAL : Equilibrium.Type.STABLE_NODE;
        }
        return spiral ? Equilibrium.Type.UNSTABLE_SPIRAL : Equilibrium.Type.UNSTABLE_NODE;
    }

    // ---------------------------------------------------------------- lines

    private static List<Equilibrium> lines(DifferentialEquation eq, Viewport view) {
        List<Equilibrium> out = new ArrayList<>();
        double mid = (view.getXMin() + view.getXMax()) / 2;
        double step = view.height() / LINE_SCAN;
        double prevV = view.getYMin();
        double prev = eq.dv(mid, prevV);
        for (int i = 1; i <= LINE_SCAN && out.size() < MAX_POINTS; i++) {
            double v = view.getYMin() + i * step;
            double now = eq.dv(mid, v);
            Double root = null;
            if (prev == 0) {
                root = prevV;
            } else if (Double.isFinite(prev) && Double.isFinite(now) && Math.signum(prev) * Math.signum(now) < 0) {
                root = bisect(eq, mid, prevV, v);
            }
            if (root != null && flatEverywhere(eq, view, root) && (out.isEmpty()
                    || Math.abs(out.get(out.size() - 1).getV() - root) > 2 * step)) {
                out.add(Equilibrium.line(root, lineType(eq, view, root, step)));
            }
            prevV = v;
            prev = now;
        }
        return out;
    }

    private static double bisect(DifferentialEquation eq, double h, double lo, double hi) {
        double fLo = eq.dv(h, lo);
        for (int k = 0; k < 80; k++) {
            double m = (lo + hi) / 2;
            double fm = eq.dv(h, m);
            if (fm == 0) {
                return m;
            }
            if (Math.signum(fm) == Math.signum(fLo)) {
                lo = m;
                fLo = fm;
            } else {
                hi = m;
            }
        }
        return (lo + hi) / 2;
    }

    // slope 0 along the whole line, not just where we looked (y' = x - y has no such line)
    private static boolean flatEverywhere(DifferentialEquation eq, Viewport view, double v) {
        double size = 0;
        for (int c = 0; c < LINE_COLUMNS; c++) {
            double h = view.getXMin() + view.width() * c / (LINE_COLUMNS - 1);
            size = Math.max(size, Math.abs(eq.dv(h, v + view.height() / 10)));
        }
        double tol = 1e-6 * Math.max(1, size);
        for (int c = 0; c < LINE_COLUMNS; c++) {
            double h = view.getXMin() + view.width() * c / (LINE_COLUMNS - 1);
            double f = eq.dv(h, v);
            if (!Double.isFinite(f) || Math.abs(f) > tol) {
                return false;
            }
        }
        return true;
    }

    // above it sloping down + below it sloping up = things fall onto it; has to hold all the way along
    private static Equilibrium.Type lineType(DifferentialEquation eq, Viewport view, double v, double step) {
        Equilibrium.Type seen = null;
        for (int c = 0; c < LINE_COLUMNS; c++) {
            double h = view.getXMin() + view.width() * (c + 0.5) / LINE_COLUMNS;
            double above = eq.dv(h, v + step);
            double below = eq.dv(h, v - step);
            Equilibrium.Type here;
            if (above < 0 && below > 0) {
                here = Equilibrium.Type.STABLE_LINE;
            } else if (above > 0 && below < 0) {
                here = Equilibrium.Type.UNSTABLE_LINE;
            } else if (above * below > 0) {
                here = Equilibrium.Type.SEMI_STABLE_LINE;
            } else {
                continue;   // flat here too, says nothing
            }
            if (seen != null && seen != here) {
                return Equilibrium.Type.LINE;
            }
            seen = here;
        }
        return seen == null ? Equilibrium.Type.LINE : seen;
    }
}
