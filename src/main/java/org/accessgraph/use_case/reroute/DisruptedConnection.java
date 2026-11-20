package org.accessgraph.use_case.reroute;

/** A disruption as plain data the presenter can format: its type, line and the two stations it lies between. */
public final class DisruptedConnection {
    private final String typeLabel;
    private final String line;
    private final String stationId;
    private final String stationName;
    private final String otherStationId;
    private final String otherStationName;

    public DisruptedConnection(String typeLabel, String line, String stationId, String stationName,
                               String otherStationId, String otherStationName) {
        this.typeLabel = typeLabel;
        this.line = line;
        this.stationId = stationId;
        this.stationName = stationName;
        this.otherStationId = otherStationId;
        this.otherStationName = otherStationName;
    }

    public String getTypeLabel() {
        return typeLabel;
    }

    public String getLine() {
        return line;
    }

    public String getStationId() {
        return stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public String getOtherStationId() {
        return otherStationId;
    }

    public String getOtherStationName() {
        return otherStationName;
    }
}
