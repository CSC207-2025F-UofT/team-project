package use_case.currentWeather;

public class CurrentWeatherInputData {
    private final String cityName;

    public CurrentWeatherInputData(String cityName) {
        this.cityName = cityName;
    }

    public String getCityName() {
        return cityName;
    }
}
