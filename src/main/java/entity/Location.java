package entity;

public class Location {
    private final double latitude;
    private final double longitude;

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public TileCoords getTileCoords(int zoom) {
        double n = 1 << zoom;
        double latRad = Math.toRadians(latitude);
        int x = (int) Math.floor((longitude + 180.0) / 360.0 * n);
        int y = (int) Math.floor((1 - Math.log(Math.tan(latRad) + 1 / Math.cos(latRad)) / Math.PI) / 2 * n);
        return new TileCoords(x, y, zoom);
    }
 }
