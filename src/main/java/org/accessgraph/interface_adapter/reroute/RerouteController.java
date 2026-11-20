package org.accessgraph.interface_adapter.reroute;

import org.accessgraph.entity.Disruption;
import org.accessgraph.use_case.reroute.RerouteInputBoundary;
import org.accessgraph.use_case.reroute.RerouteInputData;

/**
 * Turns the view's selections into input data and starts the reroute use case. The journey to
 * recalculate is the one the passenger last searched for.
 */
public final class RerouteController {
    private final RerouteInputBoundary interactor;
    private final JourneyRecorder journeys;

    public RerouteController(RerouteInputBoundary interactor, JourneyRecorder journeys) {
        this.interactor = interactor;
        this.journeys = journeys;
    }

    /**
     * Simulates a disruption on the line between two neighbouring stations.
     *
     * @param type the name of a {@link Disruption.Type}, or null when none is chosen
     */
    public void simulate(String line, String stationId, String otherStationId, String type) {
        interactor.simulate(new RerouteInputData(line, stationId, otherStationId,
                type == null ? null : Disruption.Type.valueOf(type),
                journeys.getOriginId(), journeys.getDestinationId()));
        journeys.searchAgain();
    }

    /** Makes the line between two neighbouring stations available again. */
    public void restore(String line, String stationId, String otherStationId) {
        interactor.restore(new RerouteInputData(line, stationId, otherStationId,
                journeys.getOriginId(), journeys.getDestinationId()));
        journeys.searchAgain();
    }
}
