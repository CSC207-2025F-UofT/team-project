package interface_adapter.phase;

import use_case.explore_phase.ExplorePhaseInputBoundary;

// the live phase card's mouse -> explore use case
public class PhaseExploreController {
    private final ExplorePhaseInputBoundary interactor;

    public PhaseExploreController(ExplorePhaseInputBoundary interactor) {
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

    public void trace(double h, double v, double snapH, double snapV) {
        interactor.trace(h, v, snapH, snapV);
    }

    public void endTrace() {
        interactor.endTrace();
    }

    public void toggleSolution(double h, double v, double snapH, double snapV) {
        interactor.toggleSolution(h, v, snapH, snapV);
    }
}
