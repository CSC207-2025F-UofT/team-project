package interface_adapter.Login;

import ViewManager.ViewManagerModel;
import interface_adapter.Loggedin.LoggedinState;
import interface_adapter.Loggedin.LoggedinViewModel;
import interface_adapter.Signup.SignupViewModel;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;

/**
 * The Presenter for the Login Use Case.
 */
public class LoginPresenter implements LoginOutputBoundary {

    private final ViewManagerModel viewManagerModel;
    private final LoginViewModel loginViewModel;
    private final LoggedinViewModel loggedinViewModel;

    public LoginPresenter(ViewManagerModel viewManagerModel,
                          LoginViewModel loginViewModel,
                          LoggedinViewModel loggedinViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.loginViewModel = loginViewModel;
        this.loggedinViewModel = loggedinViewModel;
    }

    @Override
    public void prepareSuccessView(LoginOutputData outputData) {
        // Hand the logged-in user to the Logged-in View.
        final LoggedinState loggedinState = new LoggedinState();
        loggedinState.setUsername(outputData.getUsername());
        loggedinState.setRole(outputData.getRole());
        loggedinViewModel.setState(loggedinState);
        loggedinViewModel.notifyStateChanged();

        // Clear the login form.
        loginViewModel.setState(new LoginState());
        loginViewModel.notifyStateChanged();

        viewManagerModel.setState(LoggedinViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final LoginState loginState = loginViewModel.getState();
        loginState.setWelcomeMessage("");
        loginState.setLoginError(errorMessage);
        loginViewModel.notifyStateChanged();
    }

    @Override
    public void switchToSignupView() {
        viewManagerModel.setState(SignupViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();
    }
}
