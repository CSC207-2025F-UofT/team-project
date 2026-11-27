package use_case.signup;

/**
 * Input Boundary for the Sign Up use case: what the controller can ask for.
 */
public interface SignupInputBoundary {

    /**
     * Tries to create a new account.
     *
     * @param inputData the username, password and repeated password
     */
    void execute(SignupInputData inputData);

    /**
     * Switches to the login screen (the "Login" link).
     */
    void switchToLoginView();
}
