package use_case.login;

/**
 * Output boundary for the Login use case (implemented by LoginPresenter).
 */
public interface LoginOutputBoundary {
    void prepareSuccessView(LoginOutputData outputData);

    void prepareFailView(String errorMessage);

    void switchToSignupView();
}
