package interface_adapter.signup;

import use_case.signup.SignUpInputBoundary;
import use_case.signup.SignUpInputData;

public class SignUpController {
    private final SignUpInputBoundary signUpInteractor;

    public SignUpController(SignUpInputBoundary signUpInteractor) {
        this.signUpInteractor = signUpInteractor;
    }

    public void execute(String userName, String email, String password,
                        String repeatPassword) {

        signUpInteractor.execute(new SignUpInputData(userName, email, password,
                        repeatPassword)
        );
    }
}
