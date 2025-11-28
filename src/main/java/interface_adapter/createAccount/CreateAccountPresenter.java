package interface_adapter.createAccount;

import interface_adapter.ViewManagerModel;
import interface_adapter.homepage.HomePageViewModel;
import interface_adapter.signin.SignInViewModel;
import use_case.createAccount.CreateAccountOutputBoundary;
import use_case.createAccount.CreateAccountOutputData;
import view.HomePageView;

public class CreateAccountPresenter implements CreateAccountOutputBoundary {

    private final CreateAccountViewModel createAccountViewModel;
    private final HomePageViewModel homePageViewModel;
    private final SignInViewModel signInViewModel;
    private final ViewManagerModel viewManagerModel;

    public CreateAccountPresenter(CreateAccountViewModel createAccountViewModel,
                                  HomePageViewModel homePageViewModel,
                                  SignInViewModel signInViewModel,
                                  ViewManagerModel viewManagerModel) {
        this.createAccountViewModel = createAccountViewModel;
        this.homePageViewModel = homePageViewModel;
        this.signInViewModel = signInViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(CreateAccountOutputData data) {
        CreateAccountState state = createAccountViewModel.getState();
        state.setUsernameError(null);
        state.setPasswordError(null);
        state.setRepeatPasswordError(null);

        state.setUsername("");
        state.setPassword("");
        state.setRepeatPassword("");

        createAccountViewModel.setState(state);

        // switch to "login view"
        viewManagerModel.setState(signInViewModel.getViewName());// need to change back to loginviewmodel
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String usernameError,
                                String passwordError,
                                String repeatPasswordError) {
        CreateAccountState state = createAccountViewModel.getState();
        state.setUsernameError(usernameError);
        state.setPasswordError(passwordError);
        state.setRepeatPasswordError(repeatPasswordError);

        createAccountViewModel.setState(state);
        createAccountViewModel.firePropertyChanged();
    }

    @Override
    public void switchToHomePage() {
        viewManagerModel.setState(homePageViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }
}
