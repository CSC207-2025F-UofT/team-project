package interface_adapter.loggedInFavouritesPage;

import entity.weatherReport.WeatherReport;

import java.util.ArrayList;
import java.util.List;

public class LoggedInFavouritesPageState {
    private List<WeatherReport> weatherReports = new ArrayList<>();


    public List<WeatherReport> getWeatherReports() {
        return weatherReports;
    }

    public void setWeatherReports(List<WeatherReport> weatherReports) { this.weatherReports = weatherReports;}

    public void clearState() {
        this.weatherReports = new ArrayList<>();
    }
}
