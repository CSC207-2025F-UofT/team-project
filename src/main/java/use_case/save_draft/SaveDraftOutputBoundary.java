package use_case.save_draft;

public interface SaveDraftOutputBoundary {
    void presentSuccess(SaveDraftOutputData outputData);
    void presentFailure(String errorMessage);
}