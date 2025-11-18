package use_case.DeleteFavouriteLocation;

/**
 * The Input Data for the DeleteLocation Use Case.
 */
public class DeleteLocationInputData {
    private final String username;
    private final String cityName;

    public DeleteLocationInputData(String username, String cityName) {
        this.cityName = cityName;
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public String getCityName() {
        return this.cityName;
    }
}
