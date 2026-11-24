package interface_adapter;

import use_case.signup.SignUpInputBoundary;
import use_case.signup.SignUpInputData;

/** Original team signup controller, updated to the redesigned use_case package. */
public class SignUpController {
    private final SignUpInputBoundary signUpInteractor;

    public SignUpController(SignUpInputBoundary signUpInteractor) {
        this.signUpInteractor = signUpInteractor;
    }

    public void execute(String userName, String password, String repeatPassword) {
        SignUpInputData inputData = new SignUpInputData(userName, password, repeatPassword);
        signUpInteractor.execute(inputData.getUserName(), inputData.getPassword(),
            inputData.getRepeatPassword());
    }
}
