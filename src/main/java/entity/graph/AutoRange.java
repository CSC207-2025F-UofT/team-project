package entity.graph;

import java.util.Arrays;

// picks a value window from samples, ignores the few that blow up (1/x near 0)
public final class AutoRange {
    // full range wider than this x the middle 90% = blow-up, use the middle
    private static final double OUTLIER_RATIO = 10;
    private static final double PAD = 0.05;

    private AutoRange() {
    }

    // {lo, hi} padded a bit, null if nothing finite came in
    public static double[] window(double[] values) {
        double[] sorted = Arrays.stream(values).filter(Double::isFinite).sorted().toArray();
        if (sorted.length == 0) {
            return null;
        }
        double lo = sorted[0];
        double hi = sorted[sorted.length - 1];
        double p5 = sorted[(int) Math.floor(0.05 * (sorted.length - 1))];
        double p95 = sorted[(int) Math.ceil(0.95 * (sorted.length - 1))];
        double central = p95 - p5;
        if (central > 0 && hi - lo > OUTLIER_RATIO * central) {
            lo = Math.max(lo, p5 - central);
            hi = Math.min(hi, p95 + central);
        }
        if (hi - lo < 1e-9) {
            // flat line, give it some room
            lo -= 1;
            hi += 1;
        }
        double pad = PAD * (hi - lo);
        return new double[]{lo - pad, hi + pad};
    }
}
