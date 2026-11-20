package org.accessgraph.interface_adapter.plan_route;

import org.accessgraph.use_case.plan_route.PlanRouteInputBoundary;
import org.accessgraph.use_case.plan_route.PlanRouteInputData;

/** Turns the view's selections into input data and starts the use case. */
public final class PlanRouteController {
    private final PlanRouteInputBoundary interactor;

    public PlanRouteController(PlanRouteInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String originId, String destinationId) {
        interactor.execute(new PlanRouteInputData(originId, destinationId));
    }
}
