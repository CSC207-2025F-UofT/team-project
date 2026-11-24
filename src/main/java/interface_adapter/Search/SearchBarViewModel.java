package interface_adapter.Search;

import java.util.ArrayList;
import java.util.List;

/**
 * The View Model for the search bar View.
 */
public class SearchBarViewModel {

    private final List<SearchViewModelListener> listeners = new ArrayList<>();

    public static final String VIEW_NAME = "search";

    private SearchState state = new SearchState();

    private SearchController searchController;

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

    public void setSearchController(SearchController searchController) {
        this.searchController = searchController;
    }
}