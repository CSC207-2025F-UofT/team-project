package interface_adapter.ingame;

/**
 * Something that wants to know when the lobby state changes.
 */
public interface InGameViewModelListener {

    /**
     * Called when the LobbyViewModel's state has changed.
     */
    void stateChanged();
}
