package use_case.adopter_login;

/**
 * Output boundary for the adopter login use case.
 */
public interface AdopterLoginOutputBoundary {
    void prepareSuccessView(AdopterLoginOutputData outputData);

    void prepareFailView(String errorMessage);
}
