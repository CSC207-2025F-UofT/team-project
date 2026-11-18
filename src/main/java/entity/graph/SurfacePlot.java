package entity.graph;

import entity.SurfaceFunction;

// z = f(x, y) over an x / y window, the viewport is the floor of the box
public final class SurfacePlot implements Plot {
    static final int GRID = 56;

    private final SurfaceFunction function;
    private final Viewport viewport;
    private final Viewport home;
    private SurfaceGrid grid;

    public SurfacePlot(SurfaceFunction function, Viewport viewport) {
        this(function, viewport, viewport);
    }

    private SurfacePlot(SurfaceFunction function, Viewport viewport, Viewport home) {
        this.function = function;
        this.viewport = viewport;
        this.home = home;
    }

    public SurfaceFunction getFunction() {
        return function;
    }

    @Override
    public String getLatex() {
        return function.getLatex();
    }

    @Override
    public Viewport getViewport() {
        return viewport;
    }

    @Override
    public Viewport getHome() {
        return home;
    }

    @Override
    public Plot lookingAt(Viewport next) {
        return new SurfacePlot(function, next, home);
    }

    public synchronized SurfaceGrid getGrid() {
        if (grid == null) {
            grid = SurfaceGrid.sample(function, viewport, GRID);
        }
        return grid;
    }

    // the real f(x, y), the picked spot is only roughly where the mouse is
    @Override
    public TracePoint trace(double x, double y, double snapX) {
        if (!viewport.containsX(x) || !viewport.containsY(y)) {
            return null;
        }
        double z = function.evaluate(x, y);
        return Double.isFinite(z) ? TracePoint.onSurface(x, y, z) : null;
    }

    @Override
    public <T> T accept(PlotVisitor<T> visitor) {
        return visitor.visitSurface(this);
    }
}
