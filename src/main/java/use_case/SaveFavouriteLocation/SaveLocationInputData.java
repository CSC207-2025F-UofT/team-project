package use_case.SaveFavouriteLocation;

/**
 * The Input Data for the SaveLocation Use Case.
 */
public class SaveLocationInputData {
    private final String username;
    private final String cityName;

    public SaveLocationInputData(String username, String cityName) {
        this.username = username;
        this.cityName = cityName;
    }

    public String getUsername() {
        return this.username;
    }

    public String getCityName() {
        return this.cityName;
    }
}
