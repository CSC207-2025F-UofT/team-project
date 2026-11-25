package use_case.save;

public interface SaveOutputBoundary {

    void prepareSuccessView(SaveOutputData outputData);

    void prepareFailView(String errorMessage);
}
