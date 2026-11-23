package interface_adapter.admin_signup;

import use_case.admin_signup.AdminSignupInputBoundary;
import use_case.admin_signup.AdminSignupInputData;

/**
 * Controller for the Admin Signup Use Case.
 */
public class AdminSignupController {

    private final AdminSignupInputBoundary userSignupUseCaseInteractor;

    public AdminSignupController(AdminSignupInputBoundary userSignupUseCaseInteractor) {
        this.userSignupUseCaseInteractor = userSignupUseCaseInteractor;
    }

    /**
     * Executes the Admin Signup Use Case.
     * @param username the username to sign up
     * @param id the id to sign up
     * @param email the email to sign up
     * @param password1 the password
     * @param password2 the password repeated
     * @param invite_code the invite code to sign up
     */
    public void execute(String username, String id, String email, String password1, String password2, String invite_code) {
        final AdminSignupInputData signupInputData = new AdminSignupInputData(
                username, id, email, password1, password2, invite_code);

        userSignupUseCaseInteractor.execute(signupInputData);
    }

    /**
     * Executes the "switch to LoginView" Use Case.
     */
    public void switchToLoginView() {
        userSignupUseCaseInteractor.switchToLoginView();
    }
}
