package interface_adapter.Login;

import use_case.login.LoginInputBoundary;
import use_case.login.LoginInputData;

/**
 * The controller for the Login Use Case.
 */
public class LoginController {
    private final LoginInputBoundary loginInteractor;

    public LoginController(LoginInputBoundary loginInteractor) {
        this.loginInteractor = loginInteractor;
    }

    /**
     * Executes the Login Use Case.
     * @param username the username to log in with
     */
    public void execute(String username) {
        // LoginInputData still carries a researcher flag, but LoginInteractor ignores it:
        // the role always comes from the stored user.
        loginInteractor.execute(new LoginInputData(username, false));
    }

    /**
     * Switches to the Signup View.
     */
    public void switchToSignupView() {
        loginInteractor.switchToSignupView();
    }
}
