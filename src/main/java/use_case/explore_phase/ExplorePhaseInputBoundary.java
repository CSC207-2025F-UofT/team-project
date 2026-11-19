package use_case.explore_phase;

// what the live phase card can ask for; snapH / snapV = how near (world units) counts as "on" something
public interface ExplorePhaseInputBoundary {
    void pan(double dx, double dy);

    void zoom(double cx, double cy, double fx, double fy);

    // home window, pinned solutions cleared
    void reset();

    void trace(double h, double v, double snapH, double snapV);

    void endTrace();

    // click: pin the solution through here, or unpin the one under the cursor
    void toggleSolution(double h, double v, double snapH, double snapV);
}
