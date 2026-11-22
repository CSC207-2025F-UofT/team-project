package interface_adapter.submit_application;

/**
 * Listens for changes to the Submit Application View Model.
 */
public interface SubmitApplicationViewModelListener {

    /**
     * The submit application state was updated.
     */
    void stateChanged();
}
