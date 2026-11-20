package org.accessgraph.interface_adapter.plan_route;

import java.util.List;

/** What the plan-route view shows: either a summary with steps, or an error. */
public final class PlanRouteState {
    private final String summary;
    private final List<String> steps;
    private final String error;

    public PlanRouteState(String summary, List<String> steps, String error) {
        this.summary = summary;
        this.steps = List.copyOf(steps);
        this.error = error;
    }

    public static PlanRouteState empty() {
        return new PlanRouteState("", List.of(), "");
    }

    public String getSummary() {
        return summary;
    }

    public List<String> getSteps() {
        return steps;
    }

    public String getError() {
        return error;
    }
}
