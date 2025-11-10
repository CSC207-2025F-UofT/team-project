package entities;

public class Location {
    private final String name;
    private final double latitude;
    private final double longitude;
    private final String countryCode;

    public Location (String name, double latitude, double longitude, String countryCode) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("City name cannot be null or empty");
        }
        if (countryCode == null || countryCode.length() != 2) {
            throw new IllegalArgumentException("Country code must be 2 letters (e.g., CA)");
        }
        if (latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90");
        }
        if (longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180");
        }

        this.name = name.trim();
        this.latitude = latitude;
        this.longitude = longitude;
        this.countryCode = countryCode.toUpperCase();
    }

    public String getName() {return name;}
    public double getLatitude() {return latitude;}
    public double getLongitude() {return longitude;}
    public String getCountryCode() {return countryCode;}

    //checking duplicates
    public String naturalKey(){
        return (name + "," + countryCode).toUpperCase();
    }

    @Override
    public String toString() {
        return name + "," + countryCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Location other)) return false;
        return naturalKey().equals(other.naturalKey());
    }

    @Override
    public int hashCode() {
        return naturalKey().hashCode();
    }
}
