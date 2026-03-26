package use_case.loggedInFavouritesPage;

import entity.weatherReport.WeatherReport;

public interface LoggedInFavouritesPageOutputBoundary {

    void switchToLoggedInHomePageView();

    void switchToWeatherReportView(WeatherReport weatherReport);
}
