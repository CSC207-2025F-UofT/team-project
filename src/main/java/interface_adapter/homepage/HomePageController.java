package interface_adapter.homepage;

import use_case.homePage.HomePageInputBoundary;
import use_case.homePage.HomePageInteractor;
import view.ViewManager;

//** This is the HomePageController. Input from the homePageView passes through here.
public class HomePageController {
    //** From the controller(here) we will go to the interactor (use case folder) **//
    private final HomePageInputBoundary homePageInputBoundary;

    public HomePageController(HomePageInputBoundary homePageInputBoundary) {
        this.homePageInputBoundary = homePageInputBoundary;
    }

    //** Switch to SignInView use case **//
    public void switchToCreateAccountView() {
        homePageInputBoundary.switchToCreateAccountView();
    }

    public void switchToLoginView() {
        homePageInputBoundary.switchToLoginView();
    }
}
