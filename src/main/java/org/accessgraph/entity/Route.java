package org.accessgraph.entity;

import java.util.List;

/** A found journey: the stations in order, the line used for each leg, its duration and transfer count. */
public final class Route {
    private final List<Station> stations;
    private final List<String> legLines;
    private final int totalMinutes;
    private final int transfers;

    public Route(List<Station> stations, List<String> legLines, int totalMinutes, int transfers) {
        if (stations.isEmpty() || legLines.size() != stations.size() - 1) {
            throw new IllegalArgumentException("A route needs one line per leg between its stations.");
        }
        this.stations = List.copyOf(stations);
        this.legLines = List.copyOf(legLines);
        this.totalMinutes = totalMinutes;
        this.transfers = transfers;
    }

    public List<Station> getStations() {
        return stations;
    }

    /** The line used to travel from station i to station i + 1. */
    public List<String> getLegLines() {
        return legLines;
    }

    public int getTotalMinutes() {
        return totalMinutes;
    }

    public int getTransfers() {
        return transfers;
    }
}
