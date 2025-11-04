package entity;

import java.time.Instant;

// 'BookmarkedLocation' Entity class which extends 'Location' Entity class.
public class BookmarkedLocation extends entity.Location {
    private final String name;
    private final Instant savedTime;

    public BookmarkedLocation(String name, double latitude, double longitude) {
        super(latitude, longitude);
        this.name = name;
        this.savedTime = Instant.now();
    }
// Getter for the private attribute 'name'.
    public String getName() {
        return name;
    }
// Getter for the private attribute 'savedTime'.
    public Instant getSavedTime() {
        return savedTime;
    }

}