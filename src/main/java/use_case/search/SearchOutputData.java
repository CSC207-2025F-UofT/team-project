package use_case.search;

/**
 * Output Data for the Login Use Case.
 */
public class SearchOutputData {
    private String locationName;
    private double latitude;
    private double longitude;

    public SearchOutputData(String locationName, double lat, double lon) {
        this.locationName = locationName;
        this.latitude = lat;
        this.longitude = lon;
    }

    public String getLocationName() {
        return locationName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
