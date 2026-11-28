package use_case.logout;

/**
 * The output boundary for the Log Out Use Case.
 */
public interface LogoutOutputBoundary {

    /**
     * Prepares the view shown after the user is logged out.
     * @param outputData the output data
     */
    void prepareSuccessView(LogoutOutputData outputData);
}
