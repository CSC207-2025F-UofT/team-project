package org.accessgraph.use_case.station_map;

import org.accessgraph.entity.GeoPosition;
import org.accessgraph.entity.Station;

import java.time.Instant;
import java.util.List;

/** Holds the current viewport and the freshest permitted device position for one sign-in. */
public final class MapSession {
    private static final int TORONTO_ZOOM = 14;
    private static final int DEVICE_ZOOM = 16;
    private final List<Station> stations;
    private MapViewport viewport;
    private GeoPosition position;
    private boolean centerOnFirstFix;

    public MapSession(List<Station> stations) {
        this.stations = List.copyOf(stations);
        reset(900, 540);
    }

    public void reset(int width, int height) {
        viewport = MapViewport.centered(43.652, -79.383, TORONTO_ZOOM, width, height);
        position = null;
        centerOnFirstFix = true;
    }

    public boolean accept(GeoPosition fix, Instant now) {
        if (!fix.isRecent(now) || position != null && fix.getObservedAt().isBefore(position.getObservedAt())) {
            return false;
        }
        boolean moved = position == null || position.getLatitude() != fix.getLatitude()
                || position.getLongitude() != fix.getLongitude();
        position = fix;
        if (centerOnFirstFix) {
            recenter(now);
            centerOnFirstFix = false;
            return true;
        }
        return moved;
    }

    public boolean recenter(Instant now) {
        if (position == null || !position.isRecent(now)) { return false; }
        viewport = MapViewport.centered(position.getLatitude(), position.getLongitude(), DEVICE_ZOOM,
                viewport.getWidth(), viewport.getHeight());
        return true;
    }

    public boolean expire(Instant now) {
        return position != null && !position.isRecent(now) && clearPosition();
    }

    public boolean clearPosition() {
        boolean hadPosition = position != null;
        position = null;
        return hadPosition;
    }

    public void centerOnNextFix() { centerOnFirstFix = true; }

    public boolean zoom(double steps, double x, double y) { return update(viewport.zoomAt(steps, x, y)); }
    public boolean zoomTo(double level, double x, double y) { return update(viewport.zoomTo(level, x, y)); }
    public void pan(double dx, double dy) { viewport = viewport.pan(dx, dy); }
    public boolean resize(int width, int height) { return update(viewport.resize(width, height)); }

    public MapViewport getViewport() { return viewport; }
    public List<Station> getStations() { return stations; }
    public GeoPosition getPosition(Instant now) {
        return position != null && position.isRecent(now) ? position : null;
    }

    private boolean update(MapViewport next) {
        if (next == viewport) { return false; }
        viewport = next;
        return true;
    }
}
