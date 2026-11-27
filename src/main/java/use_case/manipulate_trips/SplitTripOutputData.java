package use_case.manipulate_trips;

public class SplitTripOutputData {
    private final TripDetailsOutputData originalTrip;
    private final String newTripId;
    private final String newTripName;

    public SplitTripOutputData(TripDetailsOutputData originalTrip, String newTripId, String newTripName) {
        this.originalTrip = originalTrip;
        this.newTripId = newTripId;
        this.newTripName = newTripName;
    }

    /** The original trip after the split (only the photos before the split point). */
    public TripDetailsOutputData getOriginalTrip() { return originalTrip; }
    public String getNewTripId() { return newTripId; }
    public String getNewTripName() { return newTripName; }
}