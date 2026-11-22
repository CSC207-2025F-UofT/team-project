package interface_adapter.login;

import application_business_rules.login.LoginInputBoundary;
import application_business_rules.login.LoginInputData;

public final class LoginController {
    private final LoginInputBoundary inputBoundary;
    public LoginController(LoginInputBoundary inputBoundary) { this.inputBoundary = inputBoundary; }
    public void execute(String username, String password) {
        inputBoundary.execute(new LoginInputData(username, password));
    }
}
