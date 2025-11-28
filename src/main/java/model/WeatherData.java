package model;

public class WeatherData {
    private final Location location;
    private final String summary;

    public WeatherData(Location location, String summary) {
        this.location = location;
        this.summary = summary;
    }

    public Location getLocation() { return location; }

    public String getSummary() { return summary; }
}

