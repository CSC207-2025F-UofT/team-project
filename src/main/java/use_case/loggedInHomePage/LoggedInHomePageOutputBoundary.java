package use_case.loggedInHomePage;

import entity.weatherReport.WeatherReport;

import java.util.List;

public interface LoggedInHomePageOutputBoundary {
    void switchToLoggedInSearchView();

    void switchToLoggedInFavouritesView(List<WeatherReport> weatherReports);

    void switchToSettings(String username);
}
