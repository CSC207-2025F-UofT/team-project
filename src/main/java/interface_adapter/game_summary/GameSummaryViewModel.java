package interface_adapter.game_summary;

import java.util.ArrayList;
import java.util.List;

public class GameSummaryViewModel {
    private final List<GameSummaryViewModelListener> listeners = new ArrayList<>();

    public static final String VIEW_NAME = "game summary";

    private GameSummaryState state = new GameSummaryState();

    public GameSummaryState getState() {
        return state;
    }

    public void setState(GameSummaryState state) {
        this.state = state;
    }

    public void addListener(GameSummaryViewModelListener listener) {
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (GameSummaryViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}
