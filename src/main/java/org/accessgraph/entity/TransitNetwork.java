package org.accessgraph.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** A weighted graph of stations (nodes) and connections (edges). */
public final class TransitNetwork {
    private final Map<String, Station> stations = new LinkedHashMap<>();
    private final Map<String, List<Connection>> outgoing = new HashMap<>();

    public TransitNetwork(Collection<Station> stations, Collection<Connection> connections) {
        for (Station station : stations) {
            this.stations.put(station.getId(), station);
            outgoing.put(station.getId(), new ArrayList<>());
        }
        for (Connection connection : connections) {
            if (!this.stations.containsKey(connection.getFromId())
                    || !this.stations.containsKey(connection.getToId())) {
                throw new IllegalArgumentException("A connection refers to an unknown station.");
            }
            outgoing.get(connection.getFromId()).add(connection);
        }
    }

    public boolean hasStation(String stationId) {
        return stations.containsKey(stationId);
    }

    public Station getStation(String stationId) {
        return stations.get(stationId);
    }

    /** Returns only stations that have at least one connection, in catalogue order. */
    public List<Station> getConnectedStations() {
        List<Station> result = new ArrayList<>();
        for (Station station : stations.values()) {
            if (!outgoing.get(station.getId()).isEmpty()) {
                result.add(station);
            }
        }
        return result;
    }

    public List<Connection> connectionsFrom(String stationId) {
        return Collections.unmodifiableList(outgoing.getOrDefault(stationId, List.of()));
    }

    /**
     * Returns a copy of this network in which the connections affected by the disruptions are unavailable.
     * Every station is kept, and this network is not changed.
     */
    public TransitNetwork without(Collection<Disruption> disruptions) {
        List<Connection> available = new ArrayList<>();
        for (Station station : stations.values()) {
            for (Connection connection : outgoing.get(station.getId())) {
                if (disruptions.stream().noneMatch(disruption -> disruption.affects(connection))) {
                    available.add(connection);
                }
            }
        }
        return new TransitNetwork(stations.values(), available);
    }
}
