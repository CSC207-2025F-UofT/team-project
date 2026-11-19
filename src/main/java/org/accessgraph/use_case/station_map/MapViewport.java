package org.accessgraph.use_case.station_map;

/** A viewport in Web Mercator world pixels. Zoom, drag, and resize preserve the requested anchor. */
public final class MapViewport {
    public static final int TILE_SIZE = 256;
    private static final int MAX_ZOOM = 19;
    private final int zoom;
    private final int width;
    private final int height;
    private final double left;
    private final double top;

    private MapViewport(int zoom, int width, int height, double left, double top) {
        int world = TILE_SIZE << zoom;
        this.zoom = zoom;
        this.width = width;
        this.height = height;
        this.left = left % world;
        this.top = world < height ? (world - height) / 2.0 : Math.max(0, Math.min(world - height, top));
    }

    public static MapViewport centered(double latitude, double longitude, int zoom, int width, int height) {
        if (zoom < 0 || zoom > MAX_ZOOM || width < 1 || height < 1) {
            throw new IllegalArgumentException("Invalid map dimensions or zoom.");
        }
        Pixel center = project(latitude, longitude, zoom);
        return new MapViewport(zoom, width, height, center.x - width / 2.0, center.y - height / 2.0);
    }

    public int getZoom() { return zoom; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getLeft() { return (int) Math.round(left); }
    public int getTop() { return (int) Math.round(top); }
    public boolean canZoomIn() { return zoom < MAX_ZOOM; }
    public boolean canZoomOut() { return zoom > 0; }

    public MapViewport zoomAt(int steps, double x, double y) {
        int next = (int) Math.max(0, Math.min(MAX_ZOOM, (long) zoom + steps));
        if (next == zoom) { return this; }
        double scale = Math.scalb(1.0, next - zoom);
        return new MapViewport(next, width, height, (left + x) * scale - x, (top + y) * scale - y);
    }

    public MapViewport pan(double dx, double dy) {
        return new MapViewport(zoom, width, height, left - dx, top - dy);
    }

    public MapViewport resize(int newWidth, int newHeight) {
        if (newWidth == width && newHeight == height) { return this; }
        return new MapViewport(zoom, newWidth, newHeight, left + (width - newWidth) / 2.0,
                top + (height - newHeight) / 2.0);
    }

    public Pixel screenPoint(double latitude, double longitude) {
        Pixel point = project(latitude, longitude, zoom);
        int world = TILE_SIZE << zoom;
        double x = point.x - getLeft();
        x += Math.rint((width / 2.0 - x) / world) * world;
        return new Pixel(x, point.y - getTop());
    }

    private static Pixel project(double latitude, double longitude, int zoom) {
        int world = TILE_SIZE << zoom;
        double limitedLatitude = Math.max(-85.05112878, Math.min(85.05112878, latitude));
        double sin = Math.sin(Math.toRadians(limitedLatitude));
        return new Pixel((longitude + 180) / 360 * world,
                (0.5 - Math.log((1 + sin) / (1 - sin)) / (4 * Math.PI)) * world);
    }

    public static final class Pixel {
        public final double x;
        public final double y;

        private Pixel(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}
