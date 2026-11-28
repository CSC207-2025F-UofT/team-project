package interface_adapter.signup;

import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;
import use_case.signup.SignupOutputBoundary;
import use_case.signup.SignupOutputData;

/**
 * The Presenter for the Signup Use Case.
 */
public class SignupPresenter implements SignupOutputBoundary {

    private final SignupViewModel signupViewModel;
    private final LoginViewModel loginViewModel;
    private final ViewManagerModel viewManagerModel;

    public SignupPresenter(ViewManagerModel viewManagerModel,
                           SignupViewModel signupViewModel,
                           LoginViewModel loginViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.signupViewModel = signupViewModel;
        this.loginViewModel = loginViewModel;
    }

    @Override
    public void prepareSuccessView(SignupOutputData response) {
        // On success, switch to the login view.
        final LoginState loginState = loginViewModel.getState();
        loginState.setEmail(response.getEmail());
        loginViewModel.notifyStateChanged();

        viewManagerModel.setActiveView(LoginViewModel.VIEW_NAME);
    }

    @Override
    public void prepareFailView(String error) {
        final SignupState signupState = signupViewModel.getState();
        signupState.setSignupError(error);
        signupViewModel.notifyStateChanged();
    }

    @Override
    public void switchToLoginView() {
        viewManagerModel.setActiveView(LoginViewModel.VIEW_NAME);
    }
}
