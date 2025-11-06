package entity;

public class TileCoords {
    public final int x;
    public final int y;
    public final int zoom;

    public TileCoords(int x, int y, int zoom) {
        this.x = x;
        this.y = y;
        this.zoom = zoom;
    }
    public Location getLatLong() {
        double n = 1 << zoom;
        double cx = (x + 0.5) / n;
        double cy = (y + 0.5) / n;
        double lon = cx * 360.0 - 180.0;
        double lat = Math.toDegrees(Math.atan(Math.sinh(Math.PI * (1 - 2 * cy))));
        return new Location(lat, lon);
    }
}

