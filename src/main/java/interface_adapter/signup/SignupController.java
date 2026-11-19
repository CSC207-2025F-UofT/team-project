package interface_adapter.signup;

import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInputData;

/**
 * Turns signup form input into a call to the Signup use case.
 */
public class SignupController {
    private final SignupInputBoundary signupInteractor;

    public SignupController(SignupInputBoundary signupInteractor) {
        this.signupInteractor = signupInteractor;
    }

    public void execute(String username, String password, String repeatPassword) {
        signupInteractor.execute(new SignupInputData(username, password, repeatPassword));
    }

    public void switchToLoginView() {
        signupInteractor.switchToLoginView();
    }
}
