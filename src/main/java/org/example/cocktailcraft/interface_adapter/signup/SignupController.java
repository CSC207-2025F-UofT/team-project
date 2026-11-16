package org.example.cocktailcraft.interface_adapter.signup;

import org.example.cocktailcraft.use_case.signup.SignupInputBoundary;
import org.example.cocktailcraft.use_case.signup.SignupInputData;

public class SignupController {
    private final SignupInputBoundary interactor;

    public SignupController(SignupInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String username, String password, String confirmation) {
        interactor.execute(new SignupInputData(username, password, confirmation));
    }
}
