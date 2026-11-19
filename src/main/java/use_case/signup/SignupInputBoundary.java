package use_case.signup;

/**
 * Input boundary for the Signup use case.
 */
public interface SignupInputBoundary {
    void execute(SignupInputData signupInputData);

    void switchToLoginView();
}
