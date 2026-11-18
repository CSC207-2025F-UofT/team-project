package entity.graph;

// a point worth stopping on: crossing, high, low, y axis hit
public final class KeyPoint {
    public enum Kind { Y_INTERCEPT, ROOT, MINIMUM, MAXIMUM }

    private final double x;
    private final double y;
    private final Kind kind;

    public KeyPoint(double x, double y, Kind kind) {
        this.x = x;
        this.y = y;
        this.kind = kind;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Kind getKind() {
        return kind;
    }

    @Override
    public String toString() {
        return kind + "(" + x + ", " + y + ")";
    }
}
