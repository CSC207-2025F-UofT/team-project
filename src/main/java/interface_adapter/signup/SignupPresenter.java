package interface_adapter.signup;

import interface_adapter.login_signup.LoginSignupViewModel;
import use_case.signup.SignupOutputBoundary;
import use_case.signup.SignupOutputData;

public class SignupPresenter implements SignupOutputBoundary{

    private final LoginSignupViewModel loginSignupViewModel;

    public SignupPresenter(LoginSignupViewModel loginSignupViewModel) {
        this.loginSignupViewModel = loginSignupViewModel;
    }

    @Override
    public void prepareSuccessView(SignupOutputData response) {
        // On success, switch to the login view.
        final LoginSignupState state = loginSignupViewModel.getState();
        state.setUsername("");
        state.setPassword("");
        loginSignupViewModel.notifyStateChanged();
    }

    @Override
    public void prepareFailView(String error) {
        final LoginSignupState state = loginSignupViewModel.getState();
        state.setSignupError(error);
        loginSignupViewModel.notifyStateChanged();
    }
}
