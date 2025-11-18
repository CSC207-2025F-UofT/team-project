package use_case.homePage;

//** This is the HomePageInteractor (The Use Case) **//
public class HomePageInteractor implements HomePageInputBoundary {
    //**From here we can go to the presenter (interface_adapter), or do database stuff. **//
    private final HomePageOutputBoundary userPresenter;

    public HomePageInteractor(HomePageOutputBoundary homePageOutputBoundary) {
        this.userPresenter = homePageOutputBoundary;
    }

    //** Go to the CreateAccountView presenter **//
    @Override
    public void switchToCreateAccountView() {
        // The HomePagePresenter overrides this. usually it's called userPresenter
        userPresenter.switchToCreateAccountView();
    }

    @Override
    public void switchToLoginView() {
        userPresenter.switchToLoginView();
    }
}
