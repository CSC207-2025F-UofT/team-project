package interface_adapter.Signup;

import ViewManager.ViewManagerModel;
import interface_adapter.Login.LoginState;
import interface_adapter.Login.LoginViewModel;
import use_case.signup.SignupOutputBoundary;
import use_case.signup.SignupOutputData;

/**
 * The Presenter for the Signup Use Case.
 */
public class SignupPresenter implements SignupOutputBoundary {

    private final ViewManagerModel viewManagerModel;
    private final SignupViewModel signupViewModel;
    private final LoginViewModel loginViewModel;

    public SignupPresenter(ViewManagerModel viewManagerModel,
                           SignupViewModel signupViewModel,
                           LoginViewModel loginViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.signupViewModel = signupViewModel;
        this.loginViewModel = loginViewModel;
    }

    @Override
    public void prepareSuccessView(SignupOutputData outputData) {
        // Greet the new user and pre-fill the login screen with their username.
        final LoginState loginState = new LoginState();
        loginState.setUsername(outputData.getUsername());
        loginState.setWelcomeMessage("Hello new " + outputData.getRole() + "! Log in to continue.");
        loginViewModel.setState(loginState);
        loginViewModel.notifyStateChanged();

        // Clear the signup form.
        signupViewModel.setState(new SignupState());
        signupViewModel.notifyStateChanged();

        viewManagerModel.setState(LoginViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final SignupState signupState = signupViewModel.getState();
        signupState.setSignupError(errorMessage);
        signupViewModel.notifyStateChanged();
    }

    @Override
    public void switchToLoginView() {
        viewManagerModel.setState(LoginViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();
    }
}
