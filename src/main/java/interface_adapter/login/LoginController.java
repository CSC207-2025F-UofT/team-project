package interface_adapter.login;

import usecase.login.LoginInputBoundary;
import usecase.login.LoginInputData;

public class LoginController {
    private final LoginInputBoundary loginUseCaseInteractor;

    public LoginController(LoginInputBoundary loginUseCaseInteractor) {
        this.loginUseCaseInteractor = loginUseCaseInteractor;
    }

    public void execute(String username, String password) {
        final LoginInputData loginInputData = new LoginInputData(username, password);

        loginUseCaseInteractor.execute(loginInputData);
    }
}
