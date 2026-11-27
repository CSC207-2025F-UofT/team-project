package interface_adapter.login;

import use_case.login.LoginInputBoundary;
import use_case.login.LoginInputData;

/**
 * The Controller for the Login use case: turns button clicks into use case calls.
 */
public class LoginController {

    private final LoginInputBoundary loginInteractor;

    /**
     * Creates the controller.
     *
     * @param loginInteractor the use case to call
     */
    public LoginController(LoginInputBoundary loginInteractor) {
        this.loginInteractor = loginInteractor;
    }

    /**
     * Runs the Login use case.
     *
     * @param username the username typed by the user
     * @param password the password typed by the user
     */
    public void execute(String username, String password) {
        loginInteractor.execute(new LoginInputData(username, password));
    }

    /**
     * Switches to the sign up screen.
     */
    public void switchToSignupView() {
        loginInteractor.switchToSignupView();
    }
}