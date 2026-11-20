package org.accessgraph.use_case.reroute;

import java.util.List;

/**
 * What happened to the passenger's journey after a connection was disrupted or restored, as plain data
 * the presenter can format.
 */
public final class RerouteOutputData {
    /** How the change affected the journey the passenger last searched for. */
    public enum Outcome {
        /** No journey has been searched yet, so there was nothing to recalculate. */
        NO_JOURNEY,
        /** The journey's route does not use the changed connection, so it stays the same. */
        UNCHANGED,
        /**
         * The journey has a different route. After a disruption this is the replacement for a route that
         * became invalid; after a restore it is the route that uses the reopened connection.
         */
        REROUTED,
        /** The disruption made the previous route invalid and no feasible alternative exists. */
        NO_ALTERNATIVE,
        /** The journey had no feasible route before the change and still has none. */
        NO_ROUTE
    }

    private final DisruptedConnection changed;
    private final List<DisruptedConnection> activeDisruptions;
    private final Outcome outcome;
    private final List<String> stationNames;
    private final int totalMinutes;
    private final int transfers;

    /**
     * @param changed the connection that was disrupted or restored
     * @param activeDisruptions every disruption that is active after the change
     * @param outcome how the change affected the journey
     * @param stationNames the stations of the journey's current route in order, empty when there is none
     * @param totalMinutes the estimated duration of the current route
     * @param transfers the number of line changes on the current route
     */
    public RerouteOutputData(DisruptedConnection changed, List<DisruptedConnection> activeDisruptions,
                             Outcome outcome, List<String> stationNames, int totalMinutes, int transfers) {
        this.changed = changed;
        this.activeDisruptions = List.copyOf(activeDisruptions);
        this.outcome = outcome;
        this.stationNames = List.copyOf(stationNames);
        this.totalMinutes = totalMinutes;
        this.transfers = transfers;
    }

    public DisruptedConnection getChanged() {
        return changed;
    }

    public List<DisruptedConnection> getActiveDisruptions() {
        return activeDisruptions;
    }

    public Outcome getOutcome() {
        return outcome;
    }

    public List<String> getStationNames() {
        return stationNames;
    }

    public int getTotalMinutes() {
        return totalMinutes;
    }

    public int getTransfers() {
        return transfers;
    }
}
