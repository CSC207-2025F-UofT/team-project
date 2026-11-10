package entity;

import java.time.LocalDateTime;

public class Photo implements Comparable<Photo> {
    private String id;
    private Location location;
    private LocalDateTime time;

    public Photo(String id, Location location, LocalDateTime time) {
        this.id = id;
        this.location = location;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public int compareTo(Photo other) {
        return time.compareTo(other.time);
    }
}
