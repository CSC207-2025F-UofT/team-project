package use_case;

/**
 * Output Boundary (Presenter API) for Save Outfit.
 */
public interface SaveOutfitOutputBoundary {
    void prepareSuccessView(SaveOutfitOutputData data);
    void prepareFailView(String errorMessage);
}
