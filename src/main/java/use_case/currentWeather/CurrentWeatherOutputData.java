package use_case.currentWeather;

import entity.weatherReport.WeatherReport;

public class CurrentWeatherOutputData {
    private final String cityName;

    public CurrentWeatherOutputData(String cityName) {
        this.cityName = cityName;
    }

    public String getCityName() {return cityName;}
}
