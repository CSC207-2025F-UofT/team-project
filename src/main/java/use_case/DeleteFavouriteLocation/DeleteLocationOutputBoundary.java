package use_case.DeleteFavouriteLocation;

/**
 * The output boundary for the Delete Favourite Location Use Case.
 */
public interface DeleteLocationOutputBoundary {
    void prepareSuccessView(DeleteLocationOutputData outputData);
    void prepareFailView(String errorMessage);
}
