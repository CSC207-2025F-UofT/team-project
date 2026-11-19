package interface_adapter.login;

import use_case.login.LoginInputBoundary;
import use_case.login.LoginInputData;

/**
 * Turns login form input into a call to the Login use case.
 */
public class LoginController {
    private final LoginInputBoundary loginInteractor;

    public LoginController(LoginInputBoundary loginInteractor) {
        this.loginInteractor = loginInteractor;
    }

    public void execute(String username, String password) {
        loginInteractor.execute(new LoginInputData(username, password));
    }

    public void switchToSignupView() {
        loginInteractor.switchToSignupView();
    }
}
