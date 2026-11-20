package org.accessgraph.entity;

import java.util.List;
import java.util.Objects;

/**
 * A condition that makes the ride between two neighbouring stations on one line unavailable in both
 * directions. Only that connection is affected: both stations and their other connections stay usable.
 */
public final class Disruption {
    /** Why the connection is unavailable. */
    public enum Type {
        OUTAGE("Service outage"),
        CLOSURE("Planned closure");

        private final String label;

        Type(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    private final Type type;
    private final String line;
    private final String stationId;
    private final String otherStationId;

    public Disruption(Type type, String line, String stationId, String otherStationId) {
        this.type = Objects.requireNonNull(type);
        this.line = Objects.requireNonNull(line);
        this.stationId = Objects.requireNonNull(stationId);
        this.otherStationId = Objects.requireNonNull(otherStationId);
        if (stationId.equals(otherStationId)) {
            throw new IllegalArgumentException("A disruption lies between two different stations.");
        }
    }

    public Type getType() {
        return type;
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

    /** Whether this disruption is on the given line between the two stations, in either direction. */
    public boolean isBetween(String line, String firstId, String secondId) {
        return this.line.equals(line)
                && (stationId.equals(firstId) && otherStationId.equals(secondId)
                || stationId.equals(secondId) && otherStationId.equals(firstId));
    }

    /** Whether the connection cannot be used while this disruption is active. */
    public boolean affects(Connection connection) {
        return isBetween(connection.getLine(), connection.getFromId(), connection.getToId());
    }

    /** Whether the route travels over the disrupted connection. */
    public boolean affects(Route route) {
        List<Station> stations = route.getStations();
        List<String> legLines = route.getLegLines();
        for (int leg = 0; leg < legLines.size(); leg++) {
            if (isBetween(legLines.get(leg), stations.get(leg).getId(), stations.get(leg + 1).getId())) {
                return true;
            }
        }
        return false;
    }
}
