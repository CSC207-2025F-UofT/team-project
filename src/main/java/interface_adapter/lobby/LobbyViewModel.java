package interface_adapter.lobby;

import java.util.ArrayList;
import java.util.List;

/**
 * PLACEHOLDER View Model for the game lobby / main menu, where login goes on success.
 * Owned by the main menu feature: keep VIEW_NAME and the username, and extend freely.
 */
public class LobbyViewModel {

    /** The name the lobby is registered under in the ViewManager. */
    public static final String VIEW_NAME = "lobby";

    private final List<LobbyViewModelListener> listeners = new ArrayList<>();
    private LobbyState state = new LobbyState();

    public LobbyState getState() {
        return state;
    }

    public void setState(LobbyState state) {
        this.state = state;
    }

    /**
     * Registers a listener to be told when the state changes.
     *
     * @param listener the listener to add
     */
    public void addListener(LobbyViewModelListener listener) {
        listeners.add(listener);
    }

    /**
     * Tells every listener that the state has changed.
     */
    public void notifyStateChanged() {
        for (LobbyViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}
