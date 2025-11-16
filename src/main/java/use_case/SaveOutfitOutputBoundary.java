package use_case;

public interface SaveOutfitOutputBoundary {
    void prepareSuccessView(SaveOutfitOutputData outputData);
    void prepareFailView(String errorMessage);
}
