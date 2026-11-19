package entity.graph;

import entity.MathFunction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// finds the dots the graph shows: roots, highs, lows, y intercept, only the ones in the window
public final class KeyPointFinder {
    // more than this of one kind (sin x zoomed way out, y = 0) is noise, drop that kind
    static final int MAX_PER_KIND = 24;
    private static final int REFINE_STEPS = 60;
    private static final double GOLDEN = (Math.sqrt(5) - 1) / 2;

    private final int scan;

    public KeyPointFinder(int scan) {
        if (scan < 3) {
            throw new IllegalArgumentException("scan needs at least 3 steps");
        }
        this.scan = scan;
    }

    public List<KeyPoint> find(MathFunction f, Viewport view) {
        double[] xs = new double[scan + 1];
        double[] ys = new double[scan + 1];
        for (int i = 0; i <= scan; i++) {
            xs[i] = view.getXMin() + view.width() * i / scan;
            ys[i] = f.evaluate(xs[i]);
        }
        List<KeyPoint> out = new ArrayList<>();
        double y0 = view.containsX(0) ? f.evaluate(0) : Double.NaN;
        if (Double.isFinite(y0) && view.containsY(y0)) {
            out.add(new KeyPoint(0, y0, KeyPoint.Kind.Y_INTERCEPT));
        }
        addCapped(out, roots(f, xs, ys, view));
        addCapped(out, turns(f, xs, ys, view, true));
        addCapped(out, turns(f, xs, ys, view, false));
        out = dedupe(out, view.width() / scan);
        out.sort(Comparator.comparingDouble(KeyPoint::getX));
        return out;
    }

    private static void addCapped(List<KeyPoint> out, List<KeyPoint> found) {
        if (found.size() <= MAX_PER_KIND) {
            out.addAll(found);
        }
    }

    private List<KeyPoint> roots(MathFunction f, double[] xs, double[] ys, Viewport view) {
        List<KeyPoint> roots = new ArrayList<>();
        double tolerance = 1e-6 * (1 + view.height());
        for (int i = 0; i < scan; i++) {
            double fa = ys[i];
            double fb = ys[i + 1];
            if (!Double.isFinite(fa) || !Double.isFinite(fb)) {
                continue;
            }
            if (fa == 0) {
                roots.add(new KeyPoint(xs[i], 0, KeyPoint.Kind.ROOT));
            } else if ((fa < 0 && fb > 0) || (fa > 0 && fb < 0)) {
                double r = bisect(f, xs[i], xs[i + 1], fa);
                // 1/x flips sign too, but f there is huge not ~0
                if (Math.abs(f.evaluate(r)) <= tolerance) {
                    roots.add(new KeyPoint(r, 0, KeyPoint.Kind.ROOT));
                }
            }
        }
        if (Double.isFinite(ys[scan]) && ys[scan] == 0) {
            roots.add(new KeyPoint(xs[scan], 0, KeyPoint.Kind.ROOT));
        }
        return roots;
    }

    private static double bisect(MathFunction f, double a, double b, double fa) {
        for (int k = 0; k < REFINE_STEPS; k++) {
            double m = (a + b) / 2;
            double fm = f.evaluate(m);
            if (fm == 0 || !Double.isFinite(fm)) {
                return m;
            }
            if ((fm < 0) == (fa < 0)) {
                a = m;
                fa = fm;
            } else {
                b = m;
            }
        }
        return (a + b) / 2;
    }

    // max = true finds highs, else lows; three samples bending the right way then golden search
    private List<KeyPoint> turns(MathFunction f, double[] xs, double[] ys, Viewport view, boolean max) {
        List<KeyPoint> found = new ArrayList<>();
        int sign = max ? 1 : -1;
        for (int i = 1; i < scan; i++) {
            double a = ys[i - 1];
            double m = ys[i];
            double b = ys[i + 1];
            if (!Double.isFinite(a) || !Double.isFinite(m) || !Double.isFinite(b)) {
                continue;
            }
            if (sign * m > sign * a && sign * m >= sign * b) {
                double x = golden(f, xs[i - 1], xs[i + 1], sign);
                double y = f.evaluate(x);
                if (Double.isFinite(y) && view.containsY(y) && view.containsX(x)) {
                    found.add(new KeyPoint(x, y, max ? KeyPoint.Kind.MAXIMUM : KeyPoint.Kind.MINIMUM));
                }
            }
        }
        return found;
    }

    // golden section on sign * f, NaN counts as worst
    private static double golden(MathFunction f, double a, double b, int sign) {
        double c = b - GOLDEN * (b - a);
        double d = a + GOLDEN * (b - a);
        double fc = score(f, c, sign);
        double fd = score(f, d, sign);
        for (int k = 0; k < REFINE_STEPS; k++) {
            if (fc > fd) {
                b = d;
                d = c;
                fd = fc;
                c = b - GOLDEN * (b - a);
                fc = score(f, c, sign);
            } else {
                a = c;
                c = d;
                fc = fd;
                d = a + GOLDEN * (b - a);
                fd = score(f, d, sign);
            }
        }
        return (a + b) / 2;
    }

    private static double score(MathFunction f, double x, int sign) {
        double y = f.evaluate(x);
        return Double.isFinite(y) ? sign * y : Double.NEGATIVE_INFINITY;
    }

    // same spot found twice (root that's also the low of x^2), first one wins
    private static List<KeyPoint> dedupe(List<KeyPoint> points, double gap) {
        List<KeyPoint> kept = new ArrayList<>();
        for (KeyPoint p : points) {
            boolean dup = false;
            for (KeyPoint k : kept) {
                if (Math.abs(k.getX() - p.getX()) < gap && Math.abs(k.getY() - p.getY()) < 1e-9 + 1e-6 * Math.abs(k.getY())) {
                    dup = true;
                    break;
                }
            }
            if (!dup) {
                kept.add(p);
            }
        }
        return kept;
    }
}
