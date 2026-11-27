package interface_adapter.signup;

import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;
import use_case.signup.SignupOutputBoundary;
import use_case.signup.SignupOutputData;

/**
 * The Presenter for the Sign Up use case: turns results into what the screens show.
 */
public class SignupPresenter implements SignupOutputBoundary {

    private final ViewManagerModel viewManagerModel;
    private final SignupViewModel signupViewModel;
    private final LoginViewModel loginViewModel;

    /**
     * Creates the presenter.
     *
     * @param viewManagerModel switches between screens
     * @param signupViewModel the sign up screen's view model
     * @param loginViewModel the login screen's view model
     */
    public SignupPresenter(ViewManagerModel viewManagerModel,
                           SignupViewModel signupViewModel,
                           LoginViewModel loginViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.signupViewModel = signupViewModel;
        this.loginViewModel = loginViewModel;
    }

    @Override
    public void prepareSuccessView(SignupOutputData outputData) {
        // Go to the login screen with the new username already filled in
        final LoginState loginState = new LoginState();
        loginState.setUsername(outputData.getUsername());
        loginViewModel.setState(loginState);
        loginViewModel.notifyStateChanged();

        // Clear the sign up form so it's empty next time
        signupViewModel.setState(new SignupState());
        signupViewModel.notifyStateChanged();

        showView(LoginViewModel.VIEW_NAME);
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final SignupState state = signupViewModel.getState();
        state.setSignupError(errorMessage);
        signupViewModel.notifyStateChanged();
    }

    @Override
    public void switchToLoginView() {
        signupViewModel.getState().setSignupError("");
        signupViewModel.notifyStateChanged();
        showView(LoginViewModel.VIEW_NAME);
    }

    /**
     * Tells the ViewManagerModel to switch to the given screen.
     *
     * @param viewName the name of the screen to show
     */
    private void showView(String viewName) {
        viewManagerModel.setState(viewName);
        viewManagerModel.notifyViewChanged();
    }
}