package interface_adapter.ingame;

import java.util.ArrayList;
import java.util.List;

public class InGameViewModel {

    private final List<InGameViewModelListener> listeners = new ArrayList<>();

    public static final String VIEW_NAME = "in game";

    private InGameState state = new InGameState();

    public InGameState getState() {
        return this.state;
    }

    public void setState(InGameState state) {
        this.state = state;
    }

    public void addListener(InGameViewModelListener listener) {
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (InGameViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }

}