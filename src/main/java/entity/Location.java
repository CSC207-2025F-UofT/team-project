package entity;

import java.util.Objects;

/**
 * One point on the Earth, given by latitude and longitude in degrees.
 *
 * <p>This is the only class that knows how coordinates are checked and how the
 * distance between two points is measured. A Location never changes after it is
 * created.
 */
public class Location {
    private static final double EARTH_RADIUS_KM = 6371.0;
    private static final double MIN_LATITUDE = -90.0;
    private static final double MAX_LATITUDE = 90.0;
    private static final double MIN_LONGITUDE = -180.0;
    private static final double MAX_LONGITUDE = 180.0;

    private final double latitude;
    private final double longitude;

    /**
     * Creates a location.
     *
     * @param latitude degrees north, from -90 to 90
     * @param longitude degrees east, from -180 to 180
     * @throws IllegalArgumentException if either coordinate is out of range
     */
    public Location(double latitude, double longitude) {
        if (!(latitude >= MIN_LATITUDE && latitude <= MAX_LATITUDE)) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90.");
        }
        if (!(longitude >= MIN_LONGITUDE && longitude <= MAX_LONGITUDE)) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180.");
        }
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Returns the latitude in degrees.
     *
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Returns the longitude in degrees.
     *
     * @return the longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Returns the distance to another location along the Earth's surface,
     * using the haversine formula.
     *
     * @param other the other location
     * @return the distance in kilometres
     */
    public double distanceTo(Location other) {
        double lat1 = Math.toRadians(this.latitude);
        double lat2 = Math.toRadians(other.latitude);
        double deltaLat = lat2 - lat1;
        double deltaLon = Math.toRadians(other.longitude - this.longitude);

        double a = Math.pow(Math.sin(deltaLat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(deltaLon / 2), 2);

        return 2 * EARTH_RADIUS_KM * Math.asin(Math.min(1.0, Math.sqrt(a)));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Location location = (Location) o;
        return Double.compare(latitude, location.latitude) == 0
                && Double.compare(longitude, location.longitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }
}
