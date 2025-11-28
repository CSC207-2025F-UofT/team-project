package interface_adapter.loggedInFavouritesPage;

import use_case.loggedInFavouritesPage.LoggedInFavouritesPageInputBoundary;
import use_case.loggedInSearchPage.LoggedInSearchPageInputBoundary;

public class LoggedInFavouritesPageController {
    private final LoggedInFavouritesPageInputBoundary loggedInFavouritesPageInputBoundary;

    public LoggedInFavouritesPageController(LoggedInFavouritesPageInputBoundary loggedInFavouritesPageInputBoundary) {
        this.loggedInFavouritesPageInputBoundary = loggedInFavouritesPageInputBoundary;
    }

    public void switchToLoggedInHomePageView() {
        loggedInFavouritesPageInputBoundary.switchToLoggedInHomePageView();
    }

    public void execute(String location) {
        loggedInFavouritesPageInputBoundary.execute(location);
    }
}