package auth.interface_adapters.controllers;

import auth.use_case.signup.SignUpInputData;
import auth.use_case.signup.SignUpInteractor;
import auth.use_case.signup.SignUpOutputData;

public class SignUpController {

    private final SignUpInteractor signUpInteractor;

    public SignUpController(SignUpInteractor signUpInteractor) {
        this.signUpInteractor = signUpInteractor;
    }

    public SignUpOutputData signUp(String username, String password) {
        SignUpInputData input = new SignUpInputData(username, password);
        return signUpInteractor.execute(input);
    }
}
