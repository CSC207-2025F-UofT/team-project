package interface_adapter.graph;

import use_case.explore_graph.ExploreGraphInputBoundary;

// mouse on the graph card -> explore use case, everything in world units
public class GraphExploreController {
    private final ExploreGraphInputBoundary interactor;

    public GraphExploreController(ExploreGraphInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void pan(double dx, double dy) {
        interactor.pan(dx, dy);
    }

    public void zoom(double cx, double cy, double fx, double fy) {
        interactor.zoom(cx, cy, fx, fy);
    }

    public void reset() {
        interactor.reset();
    }

    public void trace(double x, double y, double snapX) {
        interactor.trace(x, y, snapX);
    }

    public void endTrace() {
        interactor.endTrace();
    }
}
