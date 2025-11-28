package use_case.loggedInSearchPage;

import entity.placeSuggestions.PlaceSuggestion;
import entity.weatherReport.WeatherReport;

import java.util.List;

public interface LoggedInSearchPageOutputBoundary {
    void switchToLoggedInHomePageView();

    void showSuggestionsToUser(List<PlaceSuggestion> suggestions);

    void switchToWeatherReportView(WeatherReport weatherReport);
}
