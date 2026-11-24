package interface_adapter.Signup;

import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInputData;

/**
 * The controller for the Signup Use Case.
 */
public class SignupController {
    private final SignupInputBoundary signupInteractor;

    public SignupController(SignupInputBoundary signupInteractor) {
        this.signupInteractor = signupInteractor;
    }

    /**
     * Executes the Signup Use Case.
     * @param username the username to register
     * @param researcher true to register a Researcher, false for a Regular User
     */
    public void execute(String username, boolean researcher) {
        signupInteractor.execute(new SignupInputData(username, researcher));
    }

    /**
     * Switches to the Login View.
     */
    public void switchToLoginView() {
        signupInteractor.switchToLoginView();
    }
}
