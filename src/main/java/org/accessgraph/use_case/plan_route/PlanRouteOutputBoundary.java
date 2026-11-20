package org.accessgraph.use_case.plan_route;

/** Implemented by the presenter; the interactor reports its result through this interface. */
public interface PlanRouteOutputBoundary {
    void prepareSuccessView(PlanRouteOutputData outputData);

    void prepareFailView(String errorMessage);
}
