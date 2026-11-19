package interface_adapter.signup;

import use_case.signup.SignupOutputBoundary;
import use_case.signup.SignupOutputData;

public class SignupPresenter implements SignupOutputBoundary {

    private final SignupViewModel signupViewModel;

    public SignupPresenter(SignupViewModel signupViewModel) {
        this.signupViewModel = signupViewModel;
    }

    @Override
    public void prepareSuccessView(
            SignupOutputData signupOutputData) {

        SignupState signupState = signupViewModel.getState();

        signupState.setUsername(
                signupOutputData.getUsername());

        signupState.setSignupError(null);

        signupViewModel.setState(signupState);
        signupViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {

        SignupState signupState = signupViewModel.getState();

        signupState.setSignupError(errorMessage);

        signupViewModel.setState(signupState);
        signupViewModel.firePropertyChanged();
    }
}
