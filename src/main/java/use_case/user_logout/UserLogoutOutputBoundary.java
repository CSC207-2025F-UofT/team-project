package use_case.user_logout;

/**
 * The output boundary for the Logout Use Case.
 */
public interface UserLogoutOutputBoundary {
    /**
     * Prepares the success view for the Logout Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(UserLogoutOutputData outputData);

}
