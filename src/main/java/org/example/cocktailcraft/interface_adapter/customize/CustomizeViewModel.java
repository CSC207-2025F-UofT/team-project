package org.example.cocktailcraft.interface_adapter.customize;

import java.util.ArrayList;
import java.util.List;

public class CustomizeViewModel {
    private CustomizeState state = new CustomizeState();
    private final List<CustomizeViewModelListener> listeners = new ArrayList<>();

    public CustomizeState getState() {
        return state;
    }

    public void setState(CustomizeState state) {
        this.state = state;
    }

    public void addListener(CustomizeViewModelListener listener) {
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (CustomizeViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}
