package entity;

import java.util.List;

public class Outfit {
    private final String name;
    private final List<String> items;
    private final String weatherProfile; // e.g. "cold & rainy", "hot & humid"
    private final String location;       // optional, can be ""

    public Outfit(String name, List<String> items, String weatherProfile, String location) {
        this.name = name;
        this.items = items;
        this.weatherProfile = weatherProfile;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public List<String> getItems() {
        return items;
    }

    public String getWeatherProfile() {
        return weatherProfile;
    }

    public String getLocation() {
        return location;
    }
}
