package interface_adapter.login;
import use_case.login.LoginInputBoundary;

public class LoginController {
    private final LoginInputBoundary loginInteractor;

    public LoginController(LoginInputBoundary loginInteractor) {
        this.loginInteractor = loginInteractor;
    }

    public void switchToRegisterView() {
        loginInteractor.switchToRegisterView();
    }
}
