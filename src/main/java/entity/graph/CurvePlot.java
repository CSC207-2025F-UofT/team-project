package entity.graph;

import entity.MathFunction;

import java.util.List;

// y = f(x) in a window, samples + key points worked out once per window
public final class CurvePlot implements Plot {
    static final int SAMPLES = 900;
    static final int KEY_SCAN = 600;

    private final MathFunction function;
    private final Viewport viewport;
    private final Viewport home;
    private Curve curve;               // lazy, plots are immutable so caching is safe
    private List<KeyPoint> keyPoints;

    public CurvePlot(MathFunction function, Viewport viewport) {
        this(function, viewport, viewport);
    }

    private CurvePlot(MathFunction function, Viewport viewport, Viewport home) {
        this.function = function;
        this.viewport = viewport;
        this.home = home;
    }

    public MathFunction getFunction() {
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
        return new CurvePlot(function, next, home);
    }

    public synchronized Curve getCurve() {
        if (curve == null) {
            curve = new CurveSampler(SAMPLES).sample(function, viewport);
        }
        return curve;
    }

    public synchronized List<KeyPoint> getKeyPoints() {
        if (keyPoints == null) {
            keyPoints = List.copyOf(new KeyPointFinder(KEY_SCAN).find(function, viewport));
        }
        return keyPoints;
    }

    // near a key point -> that point, else straight down onto f(x)
    @Override
    public TracePoint trace(double x, double y, double snapX) {
        if (!Double.isFinite(x)) {
            return null;
        }
        KeyPoint best = null;
        for (KeyPoint k : getKeyPoints()) {
            double d = Math.abs(k.getX() - x);
            if (d <= snapX && (best == null || d < Math.abs(best.getX() - x))) {
                best = k;
            }
        }
        if (best != null) {
            return TracePoint.onCurve(best.getX(), best.getY(), best.getKind());
        }
        double fx = function.evaluate(x);
        return Double.isFinite(fx) ? TracePoint.onCurve(x, fx, null) : null;
    }

    @Override
    public <T> T accept(PlotVisitor<T> visitor) {
        return visitor.visitCurve(this);
    }
}
