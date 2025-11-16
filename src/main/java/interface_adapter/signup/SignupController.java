package interface_adapter.signup;

import usecase.signup.SignupInputBoundary;
import usecase.signup.SignupInputData;

public class SignupController {
    private final SignupInputBoundary signupUseCaseInteractor;

    public SignupController(SignupInputBoundary signupUseCaseInteractor) {
        this.signupUseCaseInteractor = signupUseCaseInteractor;
    }

    public void execute(String username, String password, String email, String confirmPassword) {
        final SignupInputData signupInputData = new SignupInputData(username, password, email, confirmPassword);
        signupUseCaseInteractor.execute(signupInputData);
    }

    public void switchToLoginView() {
        signupUseCaseInteractor.switchToLoginView();
    }
}
