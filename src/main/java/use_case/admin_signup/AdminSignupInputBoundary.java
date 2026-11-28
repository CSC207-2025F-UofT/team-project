package use_case.admin_signup;

/**
 * Input Boundary for the Admin Signup Use Case.
 * Anyone with a valid admin invite code can create an admin account.
 */
public interface AdminSignupInputBoundary {

    /**
     * Executes the admin signup use case.
     * @param adminSignupInputData the input data
     */
    void execute(AdminSignupInputData adminSignupInputData);

    /**
     * Executes the switch to login view use case.
     */
    void switchToLoginView();
}
