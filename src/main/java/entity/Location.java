package entity;

import java.util.Objects;

public class Location {
    private static final double EARTH_RADIUS_KM = 6371.0;
    private final double latitude;
    private final double longitude;
    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        if (!(latitude >= -90 && latitude <= 90)) {
            throw new  IllegalArgumentException("Latitude must be between -90 and 90.");
        }
    }
    public double getLatitude() {
        return latitude;
    }
    public double getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Double.compare(latitude, location.latitude) == 0 && Double.compare(longitude, location.longitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }

    public double distanceTo(Location other) {
        double lat1 = Math.toRadians(this.latitude);
        double lat2 = Math.toRadians(other.latitude);
        double dLat = lat2 - lat1;
        double dLon = Math.toRadians(other.longitude - this.longitude);

        double a = Math.pow(Math.sin(dLat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dLon / 2), 2);

        return 2 * EARTH_RADIUS_KM * Math.asin(Math.min(1.0, Math.sqrt(a)));
    }
}
