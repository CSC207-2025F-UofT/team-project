package entity.graph;

import java.util.Objects;

// the x / y window a plot looks at, immutable so every pan / zoom is a new one
public final class Viewport {
    // stop zooming before doubles turn to mush, or out to silly sizes
    static final double MIN_SPAN = 1e-6;
    static final double MAX_SPAN = 1e7;

    private final double xMin;
    private final double xMax;
    private final double yMin;
    private final double yMax;

    public Viewport(double xMin, double xMax, double yMin, double yMax) {
        if (!Double.isFinite(xMin) || !Double.isFinite(xMax) || !Double.isFinite(yMin) || !Double.isFinite(yMax)) {
            throw new IllegalArgumentException("viewport bounds must be finite");
        }
        if (!(xMin < xMax) || !(yMin < yMax)) {
            throw new IllegalArgumentException("viewport min must be below max");
        }
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
    }

    public double getXMin() {
        return xMin;
    }

    public double getXMax() {
        return xMax;
    }

    public double getYMin() {
        return yMin;
    }

    public double getYMax() {
        return yMax;
    }

    public double width() {
        return xMax - xMin;
    }

    public double height() {
        return yMax - yMin;
    }

    public boolean containsX(double x) {
        return x >= xMin && x <= xMax;
    }

    public boolean containsY(double y) {
        return y >= yMin && y <= yMax;
    }

    // slide the window, world units
    public Viewport panned(double dx, double dy) {
        if (!Double.isFinite(dx) || !Double.isFinite(dy)) {
            return this;
        }
        return new Viewport(xMin + dx, xMax + dx, yMin + dy, yMax + dy);
    }

    // zoom both axes around (cx, cy), factor < 1 goes in
    public Viewport zoomed(double cx, double cy, double factor) {
        return zoomed(cx, cy, factor, factor);
    }

    // separate x / y factors, (cx, cy) stays put on screen
    public Viewport zoomed(double cx, double cy, double fx, double fy) {
        if (!(fx > 0) || !(fy > 0) || !Double.isFinite(fx) || !Double.isFinite(fy)) {
            return this;
        }
        fx = clampFactor(width(), fx);
        fy = clampFactor(height(), fy);
        return new Viewport(cx - (cx - xMin) * fx, cx + (xMax - cx) * fx,
                cy - (cy - yMin) * fy, cy + (yMax - cy) * fy);
    }

    private static double clampFactor(double span, double factor) {
        double next = span * factor;
        if (next < MIN_SPAN) {
            return MIN_SPAN / span;
        }
        if (next > MAX_SPAN) {
            return MAX_SPAN / span;
        }
        return factor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Viewport)) {
            return false;
        }
        Viewport v = (Viewport) o;
        return v.xMin == xMin && v.xMax == xMax && v.yMin == yMin && v.yMax == yMax;
    }

    @Override
    public int hashCode() {
        return Objects.hash(xMin, xMax, yMin, yMax);
    }

    @Override
    public String toString() {
        return "x[" + xMin + ", " + xMax + "] y[" + yMin + ", " + yMax + "]";
    }
}
