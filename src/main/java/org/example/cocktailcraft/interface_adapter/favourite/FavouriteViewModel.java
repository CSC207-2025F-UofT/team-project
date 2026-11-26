package org.example.cocktailcraft.interface_adapter.favourite;

import java.util.ArrayList;
import java.util.List;

public class FavouriteViewModel {
    private final List<FavouriteViewModelListener> listeners = new ArrayList<>();

    private FavouriteState state = new FavouriteState();

    public FavouriteState getState() {
        return this.state;
    }

    public void setState(FavouriteState state) {
        this.state = state;
    }

    public void addListener(FavouriteViewModelListener listener) {
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (FavouriteViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}
