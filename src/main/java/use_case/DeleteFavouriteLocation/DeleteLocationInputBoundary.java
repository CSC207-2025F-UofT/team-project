package use_case.DeleteFavouriteLocation;

/**
 * Input Boundary for actions which are related to deleting a favourite location.
 */
public interface DeleteLocationInputBoundary {
    void execute(DeleteLocationInputData deleteInputData);
}