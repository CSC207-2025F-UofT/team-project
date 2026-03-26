package use_case.signIn;

/**
 * The output boundary for the Signin Use Case.
 */
public interface SignInOutputBoundary {
    void prepareSuccessView(SignInOutputData outputData);
    void prepareFailView(String errorMessage);
    void switchToHomePage();
}