package use_case.manipulate_trips;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Everything the trip details screen needs about one trip.
 * Returned by viewTrip, and inside the split and merge results.
 */
public class TripDetailsOutputData {
    private final String tripId;
    private final String name;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final List<PhotoDetails> photos;
    private final List<TripReference> otherTrips;

    public TripDetailsOutputData(String tripId, String name, LocalDateTime startTime, LocalDateTime endTime,
                                 List<PhotoDetails> photos, List<TripReference> otherTrips) {
        this.tripId = tripId;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.photos = List.copyOf(photos);
        this.otherTrips = List.copyOf(otherTrips);
    }

    public String getTripId() { return tripId; }
    public String getName() { return name; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    /** Chronological; photos without a time last. */
    public List<PhotoDetails> getPhotos() { return photos; }
    /** Every other trip, newest first. */
    public List<TripReference> getOtherTrips() { return otherTrips; }
}