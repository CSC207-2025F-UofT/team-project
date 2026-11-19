package interface_adapter.login;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.signup.SignupViewModel;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;

/**
 * On success, switches to the logged-in screen (the dashboard).
 * On failure, shows the error on the login screen.
 */
public class LoginPresenter implements LoginOutputBoundary {
    private final ViewManagerModel viewManagerModel;
    private final LoginViewModel loginViewModel;
    private final LoggedInViewModel loggedInViewModel;
    private final SignupViewModel signupViewModel;

    public LoginPresenter(ViewManagerModel viewManagerModel,
                          LoginViewModel loginViewModel,
                          LoggedInViewModel loggedInViewModel,
                          SignupViewModel signupViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.loginViewModel = loginViewModel;
        this.loggedInViewModel = loggedInViewModel;
        this.signupViewModel = signupViewModel;
    }

    @Override
    public void prepareSuccessView(LoginOutputData outputData) {
        LoggedInState loggedInState = loggedInViewModel.getState();
        loggedInState.setUsername(outputData.getUsername());
        loggedInState.setError(null);
        loggedInViewModel.firePropertyChanged();

        LoginState loginState = loginViewModel.getState();
        loginState.setError(null);
        loginViewModel.firePropertyChanged();

        viewManagerModel.setState(loggedInViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        LoginState loginState = loginViewModel.getState();
        loginState.setError(errorMessage);
        loginViewModel.firePropertyChanged();
    }

    @Override
    public void switchToSignupView() {
        viewManagerModel.setState(signupViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }
}
