package use_case.signup;

/**
 * Output boundary for the Signup use case (implemented by SignupPresenter).
 */
public interface SignupOutputBoundary {
    void prepareSuccessView(SignupOutputData outputData);

    void prepareFailView(String errorMessage);

    void switchToLoginView();
}
