package user_case.SignUp;

public interface SignupInputBoundary {
    void execute(SignupInputData inputData);
    void switchToLoginView();
}
