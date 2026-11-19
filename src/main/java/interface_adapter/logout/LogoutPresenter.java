package interface_adapter.logout;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;
import use_case.logout.LogoutOutputBoundary;
import use_case.logout.LogoutOutputData;

/**
 * On success, clears the dashboard and returns to the login screen.
 */
public class LogoutPresenter implements LogoutOutputBoundary {
    private final ViewManagerModel viewManagerModel;
    private final LoggedInViewModel loggedInViewModel;
    private final LoginViewModel loginViewModel;

    public LogoutPresenter(ViewManagerModel viewManagerModel,
                           LoggedInViewModel loggedInViewModel,
                           LoginViewModel loginViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.loggedInViewModel = loggedInViewModel;
        this.loginViewModel = loginViewModel;
    }

    @Override
    public void prepareSuccessView(LogoutOutputData outputData) {
        LoggedInState loggedInState = loggedInViewModel.getState();
        loggedInState.setUsername("");
        loggedInState.setError(null);
        loggedInViewModel.firePropertyChanged();

        LoginState loginState = loginViewModel.getState();
        loginState.setUsername(outputData.getUsername());
        loginState.setError(null);
        loginViewModel.firePropertyChanged();

        viewManagerModel.setState(loginViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        LoggedInState loggedInState = loggedInViewModel.getState();
        loggedInState.setError(errorMessage);
        loggedInViewModel.firePropertyChanged();
    }
}
