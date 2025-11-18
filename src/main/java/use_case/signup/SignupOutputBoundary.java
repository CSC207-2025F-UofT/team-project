package use_case.signup;

/**
 * The output boundary for the Signup Use Case.
 */
public interface SignupOutputBoundary {
    void prepareSuccessView(SignupOutputData outputData);
    void prepareFailView(String errorMessage);
}
