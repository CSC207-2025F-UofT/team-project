package use_case.loggedInHomePage;

import use_case.homePage.HomePageOutputBoundary;

public class LoggedInHomePageInteractor implements LoggedInHomePageInputBoundary {
    private final LoggedInHomePageOutputBoundary userPresenter;

    public LoggedInHomePageInteractor(LoggedInHomePageOutputBoundary loggedInHomePageOutputBoundary) {
        this.userPresenter = loggedInHomePageOutputBoundary;
    }

    @Override
    public void switchToLoggedInSearchView() {
        userPresenter.switchToLoggedInSearchView();
    }

    @Override
    public void switchToLoggedInFavouritesView() {
        userPresenter.switchToLoggedInFavouritesView();
    }
}
