package interface_adapter.login;

import use_case.login.LoginInputBoundary;
import use_case.login.LoginInputData;

public final class LoginController {
    private final LoginInputBoundary inputBoundary;
    public LoginController(LoginInputBoundary inputBoundary) { this.inputBoundary = inputBoundary; }
    public void execute(String username, String password) {
        inputBoundary.execute(new LoginInputData(username, password));
    }
}
