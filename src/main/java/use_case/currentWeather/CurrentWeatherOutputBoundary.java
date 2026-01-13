package use_case.currentWeather;

import entity.weatherReport.WeatherReport;
import interface_adapter.weatherReportPage.WeatherReportPageState;

public interface CurrentWeatherOutputBoundary {
    void switchToLoggedInSearchView();

    void switchToLoggedInHomePageView();

    void switchToCheckOutfitView(WeatherReportPageState state);

    void addToFavouriteSuccess(CurrentWeatherOutputData outputData);

    void addToFavouriteFail(CurrentWeatherOutputData outputData);

    void resetPopUpMessage();
}
