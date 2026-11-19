package interface_adapter.graph;

// a key point dot on the curve, caption like "Minimum"
public final class GraphMarker {
    private final double x;
    private final double y;
    private final String caption;

    public GraphMarker(double x, double y, String caption) {
        this.x = x;
        this.y = y;
        this.caption = caption;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getCaption() {
        return caption;
    }
}
