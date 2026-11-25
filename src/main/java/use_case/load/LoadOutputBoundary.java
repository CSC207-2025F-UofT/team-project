package use_case.load;

public interface LoadOutputBoundary {
    void prepareSuccessView(LoadOutputData outputData);

    void prepareFailView(String errorMessage);
}