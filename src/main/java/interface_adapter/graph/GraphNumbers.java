package interface_adapter.graph;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// number -> short text for ticks + readouts, and nice 1 / 2 / 5 tick steps
public final class GraphNumbers {
    private GraphNumbers() {
    }

    // 4 sig figs, no trailing zeros, sci notation when huge / tiny
    public static String format(double v) {
        if (Double.isNaN(v)) {
            return "undefined";
        }
        if (Double.isInfinite(v)) {
            return v > 0 ? "∞" : "-∞";
        }
        if (Math.abs(v) < 1e-10) {
            return "0";
        }
        double abs = Math.abs(v);
        if (abs >= 1e6 || abs < 1e-4) {
            return String.format(Locale.ROOT, "%.3e", v).replaceAll("\\.?0+e", "e").replace("e+0", "e").replace("e-0", "e-").replace("e+", "e");
        }
        return new BigDecimal(v).round(new MathContext(4)).stripTrailingZeros().toPlainString();
    }

    // tick label: as many decimals as the step needs, so 0.5 steps don't print 0.50000001
    public static String tickLabel(double v, double step) {
        if (Math.abs(v) < step * 1e-6) {
            return "0";
        }
        double abs = Math.abs(v);
        if (abs >= 1e6 || step < 1e-4) {
            return format(v);
        }
        int decimals = Math.max(0, (int) Math.ceil(-Math.log10(step) - 1e-9));
        return String.format(Locale.ROOT, "%." + decimals + "f", v);
    }

    // 1, 2, 5 x 10^k closest above raw
    public static double niceStep(double raw) {
        if (!(raw > 0) || !Double.isFinite(raw)) {
            return 1;
        }
        double exp = Math.floor(Math.log10(raw));
        double base = Math.pow(10, exp);
        double f = raw / base;
        double nice = f <= 1 ? 1 : f <= 2 ? 2 : f <= 5 ? 5 : 10;
        return nice * base;
    }

    // about `target` ticks inside [min, max]
    public static List<GraphTick> ticks(double min, double max, int target) {
        List<GraphTick> out = new ArrayList<>();
        if (!(max > min) || target < 1) {
            return out;
        }
        double step = niceStep((max - min) / target);
        double first = Math.ceil(min / step) * step;
        for (int i = 0; ; i++) {
            double v = first + i * step;
            if (v > max + step * 1e-9 || i > 1000) {
                break;
            }
            out.add(new GraphTick(v, tickLabel(v, step)));
        }
        return out;
    }
}
