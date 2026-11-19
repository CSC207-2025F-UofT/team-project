package org.accessgraph.use_case.station_map;

/** The top-left world pixel of the map at an OpenStreetMap zoom level. */
public final class MapViewport {
    public static final int TILE_SIZE = 256;
    public static final int WIDTH = 640;
    public static final int HEIGHT = 400;
    private static final int MAX_ZOOM = 19;
    private final int zoom;
    private final double left;
    private final double top;

    public MapViewport(int zoom, double left, double top) {
        if (zoom < 0 || zoom > MAX_ZOOM || !Double.isFinite(left) || !Double.isFinite(top)) {
            throw new IllegalArgumentException("Invalid map viewport.");
        }
        int worldSize = TILE_SIZE << zoom;
        this.zoom = zoom;
        this.left = left % worldSize;
        this.top = worldSize < HEIGHT ? (worldSize - HEIGHT) / 2.0
                : Math.max(0, Math.min(worldSize - HEIGHT, top));
    }

    public int getZoom() { return zoom; }
    public int getLeft() { return (int) Math.round(left); }
    public int getTop() { return (int) Math.round(top); }

    /** Keep the world point under the cursor at the same image pixel when changing scale. */
    public MapViewport zoomAt(int steps, double x, double y) {
        int next = (int) Math.max(0, Math.min(MAX_ZOOM, (long) zoom + steps));
        if (next == zoom) { return this; }
        double scale = Math.scalb(1.0, next - zoom);
        return new MapViewport(next, (left + x) * scale - x, (top + y) * scale - y);
    }
}
