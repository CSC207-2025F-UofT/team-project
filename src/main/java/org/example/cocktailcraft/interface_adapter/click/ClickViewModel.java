package org.example.cocktailcraft.interface_adapter.click;

import java.util.ArrayList;
import java.util.List;

public class ClickViewModel {
    private final List<ClickViewModelListener> listeners = new ArrayList<>();

    public static final String CLICK_NAME = "click";

    private ClickState state = new ClickState();

    public ClickState getState() {
        return this.state;
    }

    public void setState(ClickState state) {
        this.state = state;
    }

    public void addListener(ClickViewModelListener listener) {
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (ClickViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}
