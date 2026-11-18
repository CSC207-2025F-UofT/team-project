package entity.graph;

// where the cursor lands on the graph, the real f value not the pixel, z is NaN on 2d
public final class TracePoint {
    private final double x;
    private final double y;
    private final double z;
    private final KeyPoint.Kind kind;   // null = plain point, not a special one

    public TracePoint(double x, double y, double z, KeyPoint.Kind kind) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.kind = kind;
    }

    public static TracePoint onCurve(double x, double y, KeyPoint.Kind kind) {
        return new TracePoint(x, y, Double.NaN, kind);
    }

    public static TracePoint onSurface(double x, double y, double z) {
        return new TracePoint(x, y, z, null);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public KeyPoint.Kind getKind() {
        return kind;
    }

    public boolean isSurface() {
        return !Double.isNaN(z);
    }
}
