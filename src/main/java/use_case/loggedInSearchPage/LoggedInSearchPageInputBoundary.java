package use_case.loggedInSearchPage;

import api.googlePlacesAPI.PlaceFetcher;
import interface_adapter.weatherReportPage.WeatherReportPageState;

public interface LoggedInSearchPageInputBoundary {
    void switchToLoggedInHomePageView();

    void fetchSuggestions(String query) throws PlaceFetcher.PlaceNotFoundException;

    void execute(String query);
}
