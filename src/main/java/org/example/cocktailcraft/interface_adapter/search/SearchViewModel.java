package org.example.cocktailcraft.interface_adapter.search;

import java.util.ArrayList;
import java.util.List;

public class SearchViewModel {

    private final List<SearchViewModelListener> listeners = new ArrayList<>();
    private SearchState state = new SearchState();

    public SearchState getState() {
        return state;
    }

    public void setState(SearchState state) {
        this.state = state;
    }

    public void addListener(SearchViewModelListener listener) {
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (SearchViewModelListener listener : listeners) {
            listener.stateChanged();
        }

    }
}
