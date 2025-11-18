package use_case.SearchLocation;

/**
 * the Input Data for the Search Use Case
 */

public class SearchInputData {

    private final String cityName;

    public  SearchInputData(String cityName) {
        this.cityName = cityName;
    }
    String getCityName() {return cityName; }
}
