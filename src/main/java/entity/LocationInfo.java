package entity;

public class LocationInfo {
    private final String city;
    private final String county;
    private final String state;
    private final String country;
    private final String landmark;

    public LocationInfo(String city, String county, String state, String country, String landmark) {
        if (city == null || county == null || state == null || country == null) {
            // throw;
        }

        this.city = city;
        this.county = county;
        this.state = state;
        this.country = country;
        this.landmark = landmark;
    }

    public LocationInfo(String city, String county, String state, String country) {
        this(city, county, state, country, null);
    }

    public String getCity() {
        return city;
    }

    public String getCounty() {
        return county;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public String getLandmark() {
        return landmark;
    }

    public String getDisplayString() {
        return String.join(", ", city, county, state, country);
    }
}
