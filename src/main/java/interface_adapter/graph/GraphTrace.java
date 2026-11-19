package interface_adapter.graph;

// the readout under the cursor, numbers already formatted; z NaN on 2d, caption null for a plain point
public final class GraphTrace {
    private final double x;
    private final double y;
    private final double z;
    private final String caption;
    private final String xText;
    private final String yText;
    private final String zText;

    public GraphTrace(double x, double y, double z, String caption) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.caption = caption;
        this.xText = GraphNumbers.format(x);
        this.yText = GraphNumbers.format(y);
        this.zText = Double.isNaN(z) ? null : GraphNumbers.format(z);
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

    public boolean isSurface() {
        return !Double.isNaN(z);
    }

    public String getCaption() {
        return caption;
    }

    public String getXText() {
        return xText;
    }

    public String getYText() {
        return yText;
    }

    public String getZText() {
        return zText;
    }

    // "(1.5, 2.25)" or "(1, 2, 5)"
    public String getText() {
        return "(" + xText + ", " + yText + (zText == null ? "" : ", " + zText) + ")";
    }
}
