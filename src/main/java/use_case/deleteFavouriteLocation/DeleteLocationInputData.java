package use_case.deleteFavouriteLocation;

/**
 * The Input Data for the DeleteLocation Use Case.
 */
public class DeleteLocationInputData {
    private final String cityName;

    public DeleteLocationInputData(String cityName) {
        this.cityName = cityName;
    }

    public String getCityName() {
        return this.cityName;
    }
}
