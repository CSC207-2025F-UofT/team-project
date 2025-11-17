package use_case.save_draft;

public interface SaveDraftOutputBoundary {
    void prepareSuccessView(SaveDraftOutputData outputData);
    void prepareFailedView(String errorMessage, SaveDraftInputData originalInput);
}