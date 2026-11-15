package interface_adapter.login;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login_signup.LoginSignupState;
import interface_adapter.login_signup.LoginSignupViewModel;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;

/**
 * The Presenter for the Login Use Case.
 */
public class LoginPresenter implements LoginOutputBoundary{

    private final LoginSignupViewModel loginSignupViewModel;
    private final LoggedInViewModel loggedInViewModel;
    private final ViewManagerModel viewManagerModel;

    public LoginPresenter(LoginSignupViewModel loginSignupViewModel, LoggedInViewModel loggedInViewModel,
                          ViewManagerModel viewManagerModel) {
        this.loginSignupViewModel = loginSignupViewModel;
        this.loggedInViewModel = loggedInViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(LoginOutputData response) {
        // On success, update the loggedInViewModel's state
        final LoggedInState loggedInState = loggedInViewModel.getState();
        loggedInState.setUsername(response.getUsername());
        this.loggedInViewModel.notifyStateChanged();

        // and clear everything from the LoginViewModel's state
        this.loginSignupViewModel.setState(new LoginSignupState());
        this.loginSignupViewModel.notifyStateChanged();

        // switch to the logged in view
        this.viewManagerModel.setState(LoggedInViewModel.VIEW_NAME);
        this.viewManagerModel.notifyViewChanged();
    }

    @Override
    public void prepareFailView(String error) {
        final LoginSignupState loginState = loginSignupViewModel.getState();
        loginState.setLoginError(error);
        loginSignupViewModel.notifyStateChanged();
    }
}
