package use_case.login;

/**
 * Input Boundary for the Login use case: what the controller can ask for.
 */
public interface LoginInputBoundary {

    /**
     * Tries to log in with the given username and password.
     *
     * @param inputData the username and password typed by the user
     */
    void execute(LoginInputData inputData);

    /**
     * Switches to the sign up screen (the "Sign Up" link).
     */
    void switchToSignupView();
}
