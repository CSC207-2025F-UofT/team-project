package use_case.manipulate_trips;

public class RenameTripOutputData {
    private final String tripId;
    private final String newTripName;

    public RenameTripOutputData(String tripId, String newTripName) {
        this.tripId = tripId;
        this.newTripName = newTripName;
    }

    public String getTripId() { return tripId; }
    public String getNewTripName() { return newTripName; }
}
