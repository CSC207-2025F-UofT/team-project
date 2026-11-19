package org.accessgraph.use_case.station_map;

import org.accessgraph.entity.Station;

import java.util.List;
import java.util.Objects;

/** Snapshot of the viewport and pins for a single render. */
public final class MapRequest {
    private final List<Station> stations;
    private final MapViewport viewport;
    public MapRequest(List<Station> stations, MapViewport viewport) {
        this.stations = List.copyOf(stations);
        this.viewport = Objects.requireNonNull(viewport);
    }

    public List<Station> getStations() { return stations; }
    public MapViewport getViewport() { return viewport; }
}
