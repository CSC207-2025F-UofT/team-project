package use_case.manipulate_trips;

public class ViewTripInputData {
    private final String tripId;

    public ViewTripInputData(String tripId) {
        this.tripId = tripId;
    }

    public String getTripId() { return tripId; }
}