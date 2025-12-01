package use_case.openGame;

public interface OpenGameOutputBoundary {
    void prepareSuccessView(OpenGameOutputData outputData);
    void prepareFailView(String errorMessage);
}
