package interface_adapter.login;

import interface_adapter.ViewManagerModel;
import interface_adapter.lobby.LobbyState;
import interface_adapter.lobby.LobbyViewModel;
import interface_adapter.signup.SignupState;
import interface_adapter.signup.SignupViewModel;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;

/**
 * The Presenter for the Login use case: turns results into what the screens show.
 */
public class LoginPresenter implements LoginOutputBoundary {

    private final ViewManagerModel viewManagerModel;
    private final LoginViewModel loginViewModel;
    private final SignupViewModel signupViewModel;
    private final LobbyViewModel lobbyViewModel;

    /**
     * Creates the presenter.
     *
     * @param viewManagerModel switches between screens
     * @param loginViewModel the login screen's view model
     * @param signupViewModel the sign up screen's view model
     * @param lobbyViewModel the lobby's view model
     */
    public LoginPresenter(ViewManagerModel viewManagerModel,
                          LoginViewModel loginViewModel,
                          SignupViewModel signupViewModel,
                          LobbyViewModel lobbyViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.loginViewModel = loginViewModel;
        this.signupViewModel = signupViewModel;
        this.lobbyViewModel = lobbyViewModel;
    }

    @Override
    public void prepareSuccessView(LoginOutputData outputData) {
        // Tell the lobby who logged in
        final LobbyState lobbyState = lobbyViewModel.getState();
        lobbyState.setUsername(outputData.getUsername());
        lobbyViewModel.notifyStateChanged();

        // Clear the login form so it's empty next time
        loginViewModel.setState(new LoginState());
        loginViewModel.notifyStateChanged();

        showView(LobbyViewModel.VIEW_NAME);
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final LoginState state = loginViewModel.getState();
        state.setLoginError(errorMessage);
        loginViewModel.notifyStateChanged();
    }

    @Override
    public void switchToSignupView() {
        // Start with a clean sign up form and no leftover login error
        loginViewModel.getState().setLoginError("");
        loginViewModel.notifyStateChanged();
        signupViewModel.setState(new SignupState());
        signupViewModel.notifyStateChanged();

        showView(SignupViewModel.VIEW_NAME);
    }

    private void showView(String viewName) {
        viewManagerModel.setState(viewName);
        viewManagerModel.notifyViewChanged();
    }
}