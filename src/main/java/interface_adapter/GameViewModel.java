package interface_adapter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import use_case.GameOutputData;

/** Stores immutable UI state. Production writers dispatch updates to the Swing EDT. */
public final class GameViewModel {
    private final PropertyChangeSupport changes = new PropertyChangeSupport(this);
    private volatile GameViewState state;

    public GameViewModel(GameViewState initialState) {
        state = Objects.requireNonNull(initialState, "initialState");
    }

    public GameViewModel() {
        this(new GameViewState(new GameOutputData(Optional.empty(), "Escape Room Adventure", "",
                Map.of(), List.of(), List.of(), List.of(), Optional.empty(), List.of(), "",
                false, false, false), false));
    }

    public GameViewState getState() { return state; }

    public void setState(GameViewState next) {
        GameViewState previous = state;
        state = Objects.requireNonNull(next, "next");
        changes.firePropertyChange("state", previous, next);
    }

    public void setBusy(boolean busy) { setState(new GameViewState(state.data(), busy)); }
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changes.addPropertyChangeListener(listener);
    }
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changes.removePropertyChangeListener(listener);
    }
}
