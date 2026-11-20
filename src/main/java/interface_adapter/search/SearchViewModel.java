package interface_adapter.search;

import java.util.ArrayList;
import java.util.List;

/**
 * The ViewModel for the Search View.
 */
public class SearchViewModel {
    private final List<SearchViewModelListener> listeners = new ArrayList<>();

    public static final String VIEW_NAME = "search";

    private SearchState state = new SearchState();

    public SearchState getState() {
        return this.state;
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
