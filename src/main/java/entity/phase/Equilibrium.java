package entity.phase;

// a resting point of the field: (h, v) with both arrows zero, or a whole line v = c on a slope field
public final class Equilibrium {
    public enum Type {
        SADDLE, STABLE_NODE, UNSTABLE_NODE, STABLE_SPIRAL, UNSTABLE_SPIRAL, CENTER, DEGENERATE,
        STABLE_LINE, UNSTABLE_LINE, SEMI_STABLE_LINE, LINE;   // LINE = stability changes along it (y' = x y)

        public boolean isLine() {
            return this == STABLE_LINE || this == UNSTABLE_LINE || this == SEMI_STABLE_LINE || this == LINE;
        }
    }

    private final double h;   // NaN for a line, it's every h
    private final double v;
    private final Type type;

    public Equilibrium(double h, double v, Type type) {
        this.h = h;
        this.v = v;
        this.type = type;
    }

    public static Equilibrium line(double v, Type type) {
        return new Equilibrium(Double.NaN, v, type);
    }

    public double getH() {
        return h;
    }

    public double getV() {
        return v;
    }

    public Type getType() {
        return type;
    }

    public boolean isLine() {
        return type.isLine();
    }
}
