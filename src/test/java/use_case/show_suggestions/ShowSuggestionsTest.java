package use_case.show_suggestions;

import api.geocodingapi.GeocodingApiCoordinatesFetcher;
import api.googlePlacesAPI.GooglePlacesFetcher;
import api.googlePlacesAPI.PlaceFetcher;
import entity.placeSuggestions.PlaceSuggestion;
import entity.weatherReport.WeatherReport;
import org.junit.jupiter.api.Test;
import use_case.loggedInSearchPage.LoggedInSearchPageInputBoundary;
import use_case.loggedInSearchPage.LoggedInSearchPageInteractor;
import use_case.loggedInSearchPage.LoggedInSearchPageOutputBoundary;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShowSuggestionsTest {

    @Test
    void showSuggestions() throws PlaceFetcher.PlaceNotFoundException {
        GooglePlacesFetcher fetcher = new GooglePlacesFetcher();
        String query = "Tor";

        LoggedInSearchPageOutputBoundary outputBoundary = new LoggedInSearchPageOutputBoundary() {
            @Override
            public void showSuggestionsToUser(List<PlaceSuggestion> suggestions) {
                assertEquals(suggestions.get(0).getMainText(), "Toronto");
            }

            @Override
            public void switchToLoggedInHomePageView() {}
            @Override
            public void switchToWeatherReportView(WeatherReport weatherReport) {}
            @Override
            public void prepareFailToExecute(String query) {}
            @Override
            public void resetPopUpMessage() {}
        };
        LoggedInSearchPageInputBoundary interactor = new LoggedInSearchPageInteractor(
                outputBoundary,
                fetcher
        );
        interactor.fetchSuggestions(query);

    }
}
