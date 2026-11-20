package org.accessgraph.use_case.reroute;

import org.accessgraph.entity.Disruption;

/**
 * The connection whose availability changes, and the journey to recalculate afterwards. The journey's
 * station ids are null when the passenger has not searched for a route yet.
 */
public final class RerouteInputData {
    private final String line;
    private final String stationId;
    private final String otherStationId;
    private final Disruption.Type type;
    private final String originId;
    private final String destinationId;

    /** Input for simulating a disruption of the given type. */
    public RerouteInputData(String line, String stationId, String otherStationId, Disruption.Type type,
                            String originId, String destinationId) {
        this.line = line;
        this.stationId = stationId;
        this.otherStationId = otherStationId;
        this.type = type;
        this.originId = originId;
        this.destinationId = destinationId;
    }

    /** Input for restoring a connection, which needs no disruption type. */
    public RerouteInputData(String line, String stationId, String otherStationId,
                            String originId, String destinationId) {
        this(line, stationId, otherStationId, null, originId, destinationId);
    }

    public String getLine() {
        return line;
    }

    public String getStationId() {
        return stationId;
    }

    public String getOtherStationId() {
        return otherStationId;
    }

    public Disruption.Type getType() {
        return type;
    }

    public String getOriginId() {
        return originId;
    }

    public String getDestinationId() {
        return destinationId;
    }
}
