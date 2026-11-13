package entity;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LocationInfo {
    private final String city;
    private final String county;
    private final String state;
    private final String country;
    private final String landmark;

    public LocationInfo(String city, String county, String state, String country, String landmark) {
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
        return Stream.of(city, county, state, country)
            .filter(s -> s != null && !s.isBlank())
            .collect(Collectors.joining(", "));
    }

    @Override
    public String toString() {
        return getDisplayString();
    }
}
