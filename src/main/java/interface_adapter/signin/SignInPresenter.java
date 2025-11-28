package interface_adapter.signin;

import interface_adapter.ViewManagerModel;
import interface_adapter.homepage.HomePageViewModel;
import interface_adapter.loggedInHomePage.LoggedInHomePageState;
import interface_adapter.loggedInHomePage.LoggedInHomePageViewModel;
import use_case.signIn.SignInOutputBoundary;
import use_case.signIn.SignInOutputData;

public class SignInPresenter implements SignInOutputBoundary {
    private final SignInViewModel signInViewModel;
    private final HomePageViewModel homePageViewModel;
    private final LoggedInHomePageViewModel loggedInHomePageViewModel;
    private final ViewManagerModel viewManagerModel;

    public SignInPresenter(SignInViewModel signInViewModel, HomePageViewModel homePageViewModel, LoggedInHomePageViewModel loggedInHomePageViewModel, ViewManagerModel viewManagerModel) {
        this.signInViewModel = signInViewModel;
        this.homePageViewModel = homePageViewModel;
        this.loggedInHomePageViewModel = loggedInHomePageViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(SignInOutputData outputData) {
        final LoggedInHomePageState loggedInHomePageState = loggedInHomePageViewModel.getState();
        loggedInHomePageState.setUsername(outputData.getUsername());
        this.loggedInHomePageViewModel.setState(loggedInHomePageState);
        this.loggedInHomePageViewModel.firePropertyChanged();
        this.viewManagerModel.setState(loggedInHomePageViewModel.getViewName());
        this.viewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final SignInState signInState = signInViewModel.getState();
        signInState.setSigninError(errorMessage);
        signInViewModel.firePropertyChanged();
    }

    @Override
    public void switchToHomePage() {
        viewManagerModel.setState(homePageViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }
}