package use_case.graph_function;

import entity.graph.KeyPoint;

// the point under the cursor, real values; z NaN on a 2d graph, kind null for a plain point
public class TraceOutputData {
    private final double x;
    private final double y;
    private final double z;
    private final KeyPoint.Kind kind;

    public TraceOutputData(double x, double y, double z, KeyPoint.Kind kind) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.kind = kind;
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
