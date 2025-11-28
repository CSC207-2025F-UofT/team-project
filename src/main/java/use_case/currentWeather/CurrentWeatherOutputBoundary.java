package use_case.currentWeather;

import entity.weatherReport.WeatherReport;

public interface CurrentWeatherOutputBoundary {
    void switchToLoggedInSearchView();

    void switchToLoggedInHomePageView();

    void addToFavouriteSuccess(CurrentWeatherOutputData outputData);

    void addToFavouriteFail(CurrentWeatherOutputData outputData);

    void resetPopUpMessage();
}
