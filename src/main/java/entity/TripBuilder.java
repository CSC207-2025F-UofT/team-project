package entity;

import java.util.List;
import java.util.Objects;
import java.time.LocalDateTime;

public class TripBuilder {
    private String id;
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<String> photoIds;
    private List<Photo> photos;

    public TripBuilder setId(String id) {
        this.id = id;

        return this;
    }

    public TripBuilder setPhotoIds(List<String> photoIds) {
        this.photoIds = photoIds;

        return this;
    }

    public TripBuilder setPhotos(List<Photo> photos) {
        this.photos = photos;
        photoIds = photos.stream().map(Photo::getId).toList();

        return this;
    }

    public TripBuilder setName(String name) {
        this.name = name;

        return this;
    }

    public TripBuilder setTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;

        return this;
    }

    public TripBuilder generateName() {
        this.name = Trip.generateTripName(
            photos.stream()
                .map(Photo::getLocation)
                .filter(Objects::nonNull)
                .map(Location::getInfo)
                .filter(Objects::nonNull)
                .toList(),
            startTime, endTime
        );

        return this;
    }

    public TripBuilder generateTimeRange() {
        LocalDateTime start = null;
        LocalDateTime end = null;

        for (Photo photo : photos) {
            LocalDateTime time = photo.getTime();
            if (time == null) continue;

            if (start == null || time.isBefore(start)) start = time;
            if (end == null || time.isAfter(end)) end = time;
        }

        startTime = start;
        endTime = end;

        return this;
    }

    public Trip build() {
        if (id == null) throw new IllegalStateException();

        return new Trip(id, name, startTime, endTime, photoIds);
    }
}
