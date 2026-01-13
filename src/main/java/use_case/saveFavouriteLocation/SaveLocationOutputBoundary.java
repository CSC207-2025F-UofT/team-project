package use_case.saveFavouriteLocation;

/**
 * The output boundary for the Delete Favourite Location Use Case.
 */
public interface SaveLocationOutputBoundary {
    void prepareSuccessView(SaveLocationOutputData outputData);
    void prepareFailView(String errorMessage);
}
