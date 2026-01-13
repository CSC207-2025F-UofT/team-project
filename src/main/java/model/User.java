package model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private final String username;
    private final List<Location> savedLocations = new ArrayList<>();

    public User(String username) {
        this.username = username;
    }

    public void addSavedLocation(Location loc) {
        savedLocations.add(loc);
    }

    public List<Location> getSavedLocations() {
        return savedLocations;
    }

    public String getUsername() {
        return username;
    }
}
