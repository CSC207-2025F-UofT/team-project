package entity;

import java.util.List;
import java.util.Locale;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.time.Duration;

public class Trip {
    private final String id;
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private final List<String> photoIds;

    private static String getLocationLabel(List<LocationInfo> infoList) {
        LocationInfoType commonInfoType = LocationInfo.getFirstCommonInfoType(infoList);

        return commonInfoType != null ? infoList.getFirst().getType(commonInfoType) : null;
    }

    private static String getTimeSpanLabel(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) return null;

        if (start.getYear() == end.getYear()) {
            String startMonth = start.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            String endMonth = end.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);

            return start.getMonth() == end.getMonth() ?
                startMonth + " " + start.getYear() :
                startMonth + " - " + endMonth + " " + start.getYear();
        }

        return start.getYear() + " - " + end.getYear();
    }

    public static String generateTripName(List<LocationInfo> info, LocalDateTime start, LocalDateTime end) {
        String locationStr = getLocationLabel(info);
        String timeStr = getTimeSpanLabel(start, end);

        String base = locationStr != null ? "Trip to " + locationStr : "Trip";

        return timeStr != null ? base + " " + timeStr : base;
    }

    public Trip(String id, String name, LocalDateTime startTime, LocalDateTime endTime, List<String> photoIds) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.photoIds = photoIds == null ? new ArrayList<>() : new ArrayList<>(photoIds);
    }

    public Trip(String id, String name, LocalDateTime startTime, LocalDateTime endTime) {
        this(id, name, startTime, endTime, null);
    }

    public void addPhoto(Photo photo) {
        LocalDateTime photoTime = photo.getTime();

        if (photoTime != null) {
            if (startTime == null || photoTime.isBefore(startTime)) startTime = photoTime;
            if (endTime == null || photoTime.isAfter(endTime)) endTime = photoTime;
        }

        photoIds.add(photo.getId());
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    public LocalDateTime getEndTime() {
        return this.endTime;
    }

    public Duration getDuration() {
        return Duration.between(startTime, endTime);
    }

    public List<String> getPhotoIds() {
        return List.copyOf(this.photoIds);
    }
}
