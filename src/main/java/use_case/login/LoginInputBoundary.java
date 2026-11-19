package use_case.login;

/**
 * Input boundary for the Login use case.
 */
public interface LoginInputBoundary {
    void execute(LoginInputData loginInputData);

    void switchToSignupView();
}
