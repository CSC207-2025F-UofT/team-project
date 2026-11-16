package org.example.cocktailcraft.interface_adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Shared observer support. Presenters notify after changing state on the Swing EDT. */
public abstract class ViewModel<S> {
    private final List<StateChangeListener> listeners = new ArrayList<>();
    private S state;

    protected ViewModel(S initialState) {
        state = Objects.requireNonNull(initialState);
    }

    public S getState() {
        return state;
    }

    public void setState(S newState) {
        state = Objects.requireNonNull(newState);
    }

    public void addListener(StateChangeListener listener) {
        listeners.add(Objects.requireNonNull(listener));
    }

    public void removeListener(StateChangeListener listener) {
        listeners.remove(listener);
    }

    public void notifyStateChanged() {
        for (StateChangeListener listener : new ArrayList<>(listeners)) {
            listener.stateChanged();
        }
    }
}
