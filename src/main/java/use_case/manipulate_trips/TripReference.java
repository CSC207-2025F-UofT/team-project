package use_case.manipulate_trips;

import java.time.LocalDateTime;

/** Potential merge target. */
public class TripReference {
    private final String tripId;
    private final String name;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    public TripReference(String tripId, String name, LocalDateTime startTime, LocalDateTime endTime) {
        this.tripId = tripId;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getTripId() { return tripId; }
    public String getName() { return name; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
}