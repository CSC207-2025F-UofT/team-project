package interface_adapter.login;

/**
 * Something that wants to know when the login state changes (the LoginView).
 */
public interface LoginViewModelListener {

    /**
     * Called when the LoginViewModel's state has changed.
     */
    void stateChanged();
}