package interface_adapter.loggedInSearchPage;

import api.googlePlacesAPI.PlaceFetcher;
import use_case.loggedInSearchPage.LoggedInSearchPageInputBoundary;

public class LoggedInSearchPageController {
    private final LoggedInSearchPageInputBoundary loggedInSearchPageInputBoundary;

    public LoggedInSearchPageController(LoggedInSearchPageInputBoundary loggedInSearchPageInputBoundary) {
        this.loggedInSearchPageInputBoundary = loggedInSearchPageInputBoundary;
    }

    public void switchToLoggedInHomePageView() {
        loggedInSearchPageInputBoundary.switchToLoggedInHomePageView();
    }

    public void fetchSuggestions(String query) throws PlaceFetcher.PlaceNotFoundException {
        loggedInSearchPageInputBoundary.fetchSuggestions(query);
    }

    public void clearSuggestions() {
        // Intentionally left empty: when there are no search suggestions,
        // nothing needs to be cleared because fetchSuggestions() simply renders none.
    }

    public void execute(String query) {
        loggedInSearchPageInputBoundary.execute(query);
    }

    public void resetPopUpMessage() {
        loggedInSearchPageInputBoundary.resetPopUpMessage();
    }
}