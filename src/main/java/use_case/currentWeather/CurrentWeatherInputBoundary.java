package use_case.currentWeather;

import api.OpenWeatherApi.WeatherDataFetcher;
import api.geocodingapi.CoordinatesFetcher;

public interface CurrentWeatherInputBoundary {
    void switchToLoggedInSearchView();
    void switchToLoggedInHomePageView();

    void addToFavourites(CurrentWeatherInputData inputData) throws CoordinatesFetcher.CityNotFoundException, WeatherDataFetcher.CityNotFoundException;

    void resetPopUpMessage();
}
