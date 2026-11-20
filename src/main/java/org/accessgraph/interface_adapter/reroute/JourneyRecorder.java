package org.accessgraph.interface_adapter.reroute;

import org.accessgraph.use_case.plan_route.PlanRouteInputBoundary;
import org.accessgraph.use_case.plan_route.PlanRouteInputData;

/**
 * Decorator around the plan-route use case. It remembers the journey the passenger last searched for,
 * so that journey can be recalculated when a disruption is simulated or restored.
 */
public final class JourneyRecorder implements PlanRouteInputBoundary {
    private final PlanRouteInputBoundary planRoute;
    private PlanRouteInputData lastJourney;

    public JourneyRecorder(PlanRouteInputBoundary planRoute) {
        this.planRoute = planRoute;
    }

    @Override
    public void execute(PlanRouteInputData inputData) {
        lastJourney = inputData;
        planRoute.execute(inputData);
    }

    /** The origin of the last search, or null when nothing has been searched yet. */
    public String getOriginId() {
        return lastJourney == null ? null : lastJourney.getOriginId();
    }

    /** The destination of the last search, or null when nothing has been searched yet. */
    public String getDestinationId() {
        return lastJourney == null ? null : lastJourney.getDestinationId();
    }

    /** Repeats the last search so the plan-route view shows the route under the current conditions. */
    public void searchAgain() {
        if (lastJourney != null) {
            planRoute.execute(lastJourney);
        }
    }
}
