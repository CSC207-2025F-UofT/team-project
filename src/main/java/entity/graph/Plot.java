package entity.graph;

// one graph being explored: the function + the window it's looked at through
public interface Plot {
    String getLatex();

    Viewport getViewport();

    // where it started, reset goes back here
    Viewport getHome();

    // same function, new window
    Plot lookingAt(Viewport viewport);

    // snap the cursor onto the real graph, snapX = how near in x counts as on a key point, null = nothing there
    TracePoint trace(double x, double y, double snapX);

    <T> T accept(PlotVisitor<T> visitor);
}
