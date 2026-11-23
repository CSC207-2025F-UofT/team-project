package use_case.manipulate_trips;

public class SplitTripInputData {
    private final String tripId;
    private final String firstPhotoId;
    private final String newTripName;

    /**
     * @param tripId       the trip to split
     * @param firstPhotoId the first photo of the new trip; it and every later photo move to the new trip
     * @param newTripName  name for the new trip
     */
    public SplitTripInputData(String tripId, String firstPhotoId, String newTripName) {
        this.tripId = tripId;
        this.firstPhotoId = firstPhotoId;
        this.newTripName = newTripName;
    }

    public String getTripId() { return tripId; }
    public String getFirstPhotoId() { return firstPhotoId; }
    public String getNewTripName() { return newTripName; }
}