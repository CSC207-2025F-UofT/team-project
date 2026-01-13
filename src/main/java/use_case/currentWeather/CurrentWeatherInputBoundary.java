package use_case.currentWeather;

import api.openWeatherApi.WeatherDataFetcher;
import api.geocodingapi.CoordinatesFetcher;
import interface_adapter.weatherReportPage.WeatherReportPageState;

public interface CurrentWeatherInputBoundary {
    void switchToLoggedInSearchView();
    void switchToLoggedInHomePageView();
    void switchToCheckOutfitView(WeatherReportPageState state);

    void addToFavourites(CurrentWeatherInputData inputData) throws CoordinatesFetcher.CityNotFoundException, WeatherDataFetcher.CityNotFoundException;

    void resetPopUpMessage();
}
