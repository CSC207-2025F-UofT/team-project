package auth.interface_adapter.controllers;

import auth.use_case.login.LoginInputData;
import auth.use_case.signup.SignUpInputData;
import auth.use_case.signup.SignUpInteractor;
import auth.use_case.signup.SignUpOutputData;

public class SignUpController {

    private final SignUpInteractor signUpInteractor;

    public SignUpController(SignUpInteractor signUpInteractor) {
        this.signUpInteractor = signUpInteractor;
    }

    public SignUpOutputData signup(String username, String password) {
        SignUpInputData input = new SignUpInputData(username, password);
        return signUpInteractor.execute(input);
    }
}
