package entity;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.Duration;

public class Trip {
    private final String id;
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private final List<String> photoIds;

    public Trip(String id, String name, LocalDateTime startTime, LocalDateTime endTime, List<String> photoIds) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.photoIds = photoIds;
    }

    public Trip(String id, String name, LocalDateTime startTime, LocalDateTime endTime) {
        this(id, name, startTime, endTime, new ArrayList<>());
    }

    public void addPhoto(Photo photo) {
        LocalDateTime photoTime = photo.getTime();

        if (photoTime.compareTo(startTime) < 0) {
            startTime = photoTime;
        }

        if (photoTime.compareTo(endTime) > 0) {
            endTime = photoTime;
        }

        photoIds.add(photo.getId());
    }

    public Duration getDuration() {
        return Duration.between(startTime, endTime);
    }
}
