package interface_adapter.logged_in;

/**
 * Listener for changes to the LoggedInViewModel’s state.
 */
public interface LoggedInViewModelListener {
    /**
     * The loggedin state was updated.
     */
    void stateChanged();
}
