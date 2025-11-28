package use_case.SearchLocation;

/**
 * Output Data for the Search Use Case
 */
public class SearchOutputData {
    private final String cityName;
    public SearchOutputData(String cityName) {
        this.cityName = cityName;
    }
    public String getCityName() {
        return cityName;
    }
}
