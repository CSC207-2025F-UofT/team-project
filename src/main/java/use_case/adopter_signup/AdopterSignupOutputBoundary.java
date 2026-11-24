package use_case.adopter_signup;

/**
 * Output boundary for the adopter signup use case.
 */
public interface AdopterSignupOutputBoundary {
    void prepareSuccessView(AdopterSignupOutputData outputData);

    void prepareFailView(String errorMessage);
}
