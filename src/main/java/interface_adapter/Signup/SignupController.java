package interface_adapter.Signup;
import user_case.SignUp.SignupInputBoundary;
import user_case.SignUp.SignupInputData;

public class SignupController {
    private final SignupInputBoundary userSignupUseCaseInteractor;

    public SignupController(SignupInputBoundary userSignupUseCaseInteractor) {
        this.userSignupUseCaseInteractor = userSignupUseCaseInteractor;
    }

    public void execute(String username, String password1, String password2) {
        final SignupInputData signupInputData = new SignupInputData(
                username, password1, password2);

        userSignupUseCaseInteractor.execute(signupInputData);
    }

    public void switchToLoginView() {
        userSignupUseCaseInteractor.switchToLoginView();
    }
}
