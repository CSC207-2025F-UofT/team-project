package interface_adapter.loggedInHomePage;

import use_case.loggedInHomePage.LoggedInHomePageInputBoundary;

public class LoggedInHomePageController {
    private final LoggedInHomePageInputBoundary loggedInHomePageInputBoundary;

    public LoggedInHomePageController(LoggedInHomePageInputBoundary loggedInHomePageInputBoundary) {
        this.loggedInHomePageInputBoundary = loggedInHomePageInputBoundary;
    }

    public void switchToLoggedInSearchView() {
        loggedInHomePageInputBoundary.switchToLoggedInSearchView();
    }

    public void switchToLoggedInFavouritesView() {
        loggedInHomePageInputBoundary.switchToLoggedInFavouritesView();
    }
}
