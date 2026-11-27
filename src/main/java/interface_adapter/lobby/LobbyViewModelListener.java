package interface_adapter.lobby;

/**
 * Something that wants to know when the lobby state changes.
 */
public interface LobbyViewModelListener {

    /**
     * Called when the LobbyViewModel's state has changed.
     */
    void stateChanged();
}
