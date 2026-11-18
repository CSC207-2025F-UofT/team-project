package entity.graph;

import entity.MathFunction;

import java.util.ArrayList;
import java.util.List;

// walks f across the window and cuts the line at holes + asymptotes
public final class CurveSampler {
    // y gets clamped this many window heights past the edge, keeps java2d away from 1e300
    private static final double CLAMP_HEIGHTS = 4;

    private final int samples;

    public CurveSampler(int samples) {
        if (samples < 2) {
            throw new IllegalArgumentException("need at least 2 samples");
        }
        this.samples = samples;
    }

    public Curve sample(MathFunction f, Viewport view) {
        double step = view.width() / samples;
        double low = view.getYMin() - CLAMP_HEIGHTS * view.height();
        double high = view.getYMax() + CLAMP_HEIGHTS * view.height();
        List<Curve.Piece> pieces = new ArrayList<>();
        List<double[]> run = new ArrayList<>();
        double prevX = Double.NaN;
        double prevY = Double.NaN;
        // one step past each edge so the line runs off the box, not stops short
        for (int i = -1; i <= samples + 1; i++) {
            double x = view.getXMin() + i * step;
            double y = f.evaluate(x);
            if (!Double.isFinite(y)) {
                flush(run, pieces);
                prevY = Double.NaN;
                continue;
            }
            if (Double.isFinite(prevY) && jumps(f, prevX, prevY, x, y, view.height())) {
                flush(run, pieces);
            }
            run.add(new double[]{x, Math.max(low, Math.min(high, y))});
            prevX = x;
            prevY = y;
        }
        flush(run, pieces);
        return new Curve(pieces);
    }

    // big step + midpoint not between the ends = asymptote, steep but smooth stays joined
    static boolean jumps(MathFunction f, double x0, double y0, double x1, double y1, double height) {
        if (Math.abs(y1 - y0) < height) {
            return false;
        }
        double mid = f.evaluate((x0 + x1) / 2);
        if (!Double.isFinite(mid)) {
            return true;
        }
        double lo = Math.min(y0, y1);
        double hi = Math.max(y0, y1);
        double slack = 0.01 * (hi - lo);
        return mid < lo - slack || mid > hi + slack;
    }

    private static void flush(List<double[]> run, List<Curve.Piece> pieces) {
        if (run.size() >= 2) {
            double[] xs = new double[run.size()];
            double[] ys = new double[run.size()];
            for (int i = 0; i < xs.length; i++) {
                xs[i] = run.get(i)[0];
                ys[i] = run.get(i)[1];
            }
            pieces.add(new Curve.Piece(xs, ys));
        } else if (run.size() == 1) {
            // lone point, still worth a dot
            double[] p = run.get(0);
            pieces.add(new Curve.Piece(new double[]{p[0]}, new double[]{p[1]}));
        }
        run.clear();
    }
}
