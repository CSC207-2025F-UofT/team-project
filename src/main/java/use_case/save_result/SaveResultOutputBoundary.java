package use_case.save_result;

public interface SaveResultOutputBoundary {
    void prepareSuccessView(SaveResultOutputData outputData);

    void prepareFailView(String errorMessage);
}
