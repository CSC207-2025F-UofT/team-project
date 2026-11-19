package use_case.graph_function;

import entity.graph.KeyPoint;

// a dot on the curve the view can draw + snap to
public class KeyPointOutputData {
    private final double x;
    private final double y;
    private final KeyPoint.Kind kind;

    public KeyPointOutputData(double x, double y, KeyPoint.Kind kind) {
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

    public KeyPoint.Kind getKind() {
        return kind;
    }
}
