package org.example.cocktailcraft.interface_adapter.login;

import org.example.cocktailcraft.use_case.login.LoginInputBoundary;
import org.example.cocktailcraft.use_case.login.LoginInputData;

public class LoginController {
    private final LoginInputBoundary interactor;

    public LoginController(LoginInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String username, String password) {
        interactor.execute(new LoginInputData(username, password));
    }
}
