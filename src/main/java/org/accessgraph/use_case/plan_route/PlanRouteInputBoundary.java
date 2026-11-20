package org.accessgraph.use_case.plan_route;

/** Called by the controller to start the plan-route use case. */
public interface PlanRouteInputBoundary {
    void execute(PlanRouteInputData inputData);
}
