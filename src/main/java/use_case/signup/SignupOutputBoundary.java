package use_case.signup;

/**
 * Output Boundary for the Sign Up use case: how the interactor reports back.
 */
public interface SignupOutputBoundary {

    /**
     * Called when the account was created.
     *
     * @param outputData details of the new account
     */
    void prepareSuccessView(SignupOutputData outputData);

    /**
     * Called when the account could not be created.
     *
     * @param errorMessage why it failed, ready to show to the user
     */
    void prepareFailView(String errorMessage);

    /**
     * Switches to the login screen.
     */
    void switchToLoginView();
}
