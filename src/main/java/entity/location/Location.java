package entity.location;

public class Location {
    private final String name;
    private final double longitude;
    private final double latitude;

    /**
     * Creates a new location with the given non-empty name and non-empty password.
     * @param name the name of the city
     * @param longitude the longitude
     * @param latitude the latitude
     * @throws IllegalArgumentException if the name is empty
     */

    public Location(String name, double longitude, double latitude) {
        if ("".equals(name)) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }
    public String getName() {
        return name;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}