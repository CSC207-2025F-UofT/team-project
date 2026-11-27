package use_case.manipulate_trips;

public class MergeTripsOutputData {
    private final TripDetailsOutputData mergedTrip;
    private final int mergedCount;

    public MergeTripsOutputData(TripDetailsOutputData mergedTrip, int mergedCount) {
        this.mergedTrip = mergedTrip;
        this.mergedCount = mergedCount;
    }

    public TripDetailsOutputData getMergedTrip() { return mergedTrip; }
    /** How many trips went into the merge (at least 2). */
    public int getMergedCount() { return mergedCount; }
}