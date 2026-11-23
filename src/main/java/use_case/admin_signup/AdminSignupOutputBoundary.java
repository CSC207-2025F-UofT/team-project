package use_case.admin_signup;

/**
 * The output boundary for the Admin Signup Use Case.
 */
public interface AdminSignupOutputBoundary {

    /**
     * Prepares the success view for the Admin Signup Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(AdminSignupOutputData outputData);

    /**
     * Prepares the failure view for the Admin Signup Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);

    /**
     * Switches to the Login View.
     */
    void switchToLoginView();
}
