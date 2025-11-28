package interface_adapter.weatherReportPage;

import api.OpenWeatherApi.WeatherDataFetcher;
import api.geocodingapi.CoordinatesFetcher;
import use_case.currentWeather.CurrentWeatherInputBoundary;
import use_case.currentWeather.CurrentWeatherInputData;

public class WeatherReportPageController {
    private WeatherReportPageViewModel weatherReportPageViewModel;
    private CurrentWeatherInputBoundary currentWeatherInputBoundary;

    public WeatherReportPageController(CurrentWeatherInputBoundary currentWeatherInputBoundary) {
        this.currentWeatherInputBoundary = currentWeatherInputBoundary;
    }

    public void switchToLoggedInSearchView() {
        currentWeatherInputBoundary.switchToLoggedInSearchView();
    }
    public void switchToLoggedInHomePageView() {
        currentWeatherInputBoundary.switchToLoggedInHomePageView();
    }
    public void addToFavourites(WeatherReportPageState state) throws CoordinatesFetcher.CityNotFoundException, WeatherDataFetcher.CityNotFoundException {
        CurrentWeatherInputData inputData = new CurrentWeatherInputData(
                state.getCityName()
        );
        currentWeatherInputBoundary.addToFavourites(inputData);
    }

    public void resetPopUpMessage() {
        currentWeatherInputBoundary.resetPopUpMessage();
    }
}
