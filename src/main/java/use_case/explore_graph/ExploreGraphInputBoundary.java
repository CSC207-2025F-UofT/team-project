package use_case.explore_graph;

// what you can do to a graph once it's up, all in world units of the current window
public interface ExploreGraphInputBoundary {
    void pan(double dx, double dy);

    // (cx, cy) stays under the cursor, factor < 1 zooms in
    void zoom(double cx, double cy, double fx, double fy);

    // back to where the graph started
    void reset();

    // cursor at (x, y), snapX = how close in x counts as on a key point
    void trace(double x, double y, double snapX);

    void endTrace();
}
