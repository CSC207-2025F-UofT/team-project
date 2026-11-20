package org.accessgraph.use_case.plan_route;

/** The passenger's chosen origin and destination station ids. */
public final class PlanRouteInputData {
    private final String originId;
    private final String destinationId;

    public PlanRouteInputData(String originId, String destinationId) {
        this.originId = originId;
        this.destinationId = destinationId;
    }

    public String getOriginId() {
        return originId;
    }

    public String getDestinationId() {
        return destinationId;
    }
}
