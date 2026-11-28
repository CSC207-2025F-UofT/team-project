package interface_adapter.game_running;

import java.util.ArrayList;
import java.util.List;

/**
 * The View Model for the Game Running View.
 */
public class GameRunningViewModel {

    private final List<GameRunningViewModelListener> listeners = new ArrayList<>();

    public static final String VIEW_NAME = "game running";

    private GameRunningState state = new GameRunningState();

    public GameRunningState getState() {
        return state;
    }

    public void setState(GameRunningState state) {
        this.state = state;
    }

    public void addListener(GameRunningViewModelListener listener) {
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (GameRunningViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }

}
