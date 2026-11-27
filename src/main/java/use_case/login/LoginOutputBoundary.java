package use_case.login;

/**
 * Output Boundary for the Login use case: how the interactor reports back.
 */
public interface LoginOutputBoundary {

    /**
     * Called when the login succeeded.
     *
     * @param outputData details of the player who logged in
     */
    void prepareSuccessView(LoginOutputData outputData);

    /**
     * Called when the login failed.
     *
     * @param errorMessage why it failed, ready to show to the user
     */
    void prepareFailView(String errorMessage);

    /**
     * Switches to the sign up screen.
     */
    void switchToSignupView();
}
