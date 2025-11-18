package interface_adapter.homepage;


import interface_adapter.ViewManagerModel;
import interface_adapter.createAccount.CreateAccountViewModel;
import use_case.homePage.HomePageOutputBoundary;
import view.CreateAccountView;
import view.ViewManager;

//**
// This is the home page presenter, We will now update the view to reflect what we want.
// It went homePageView -> controller (interface_adapter) -> homepageInteractor (use_case) -> here.
//**
public class HomePagePresenter implements HomePageOutputBoundary {
    private final HomePageViewModel homePageViewModel;
    private final CreateAccountViewModel createAccountViewModel;
    private final ViewManagerModel viewManagerModel;

    public HomePagePresenter(HomePageViewModel homePageViewModel,
                             CreateAccountViewModel createAccountViewModel ,
                             ViewManagerModel viewManagerModel) {
        this.homePageViewModel = homePageViewModel;
        this.createAccountViewModel = createAccountViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void switchToCreateAccountView() {
        viewManagerModel.setState(createAccountViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void switchToLoginView() {
        viewManagerModel.setState(viewManagerModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }
}

