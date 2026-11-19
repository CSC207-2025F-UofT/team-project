package interface_adapter.signup;

import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;
import use_case.signup.SignupOutputBoundary;
import use_case.signup.SignupOutputData;

/**
 * On success, sends the user to the login screen with their username filled in.
 * On failure, shows the error on the signup screen.
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
    public void prepareSuccessView(SignupOutputData outputData) {
        LoginState loginState = loginViewModel.getState();
        loginState.setUsername(outputData.getUsername());
        loginState.setError(null);
        loginViewModel.firePropertyChanged();

        SignupState signupState = signupViewModel.getState();
        signupState.setError(null);
        signupViewModel.firePropertyChanged();

        switchToLoginView();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        SignupState signupState = signupViewModel.getState();
        signupState.setError(errorMessage);
        signupViewModel.firePropertyChanged();
    }

    @Override
    public void switchToLoginView() {
        viewManagerModel.setState(loginViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }
}
