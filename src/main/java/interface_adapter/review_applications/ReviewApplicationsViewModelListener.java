package interface_adapter.review_applications;

/**
 * Listens for changes to the Review Applications View Model.
 */
public interface ReviewApplicationsViewModelListener {

    /**
     * The review applications state was updated.
     */
    void stateChanged();
}
