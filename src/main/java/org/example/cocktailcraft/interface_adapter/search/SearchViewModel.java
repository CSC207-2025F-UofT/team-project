package org.example.cocktailcraft.interface_adapter.search;

import java.util.ArrayList;
import java.util.List;
import org.example.cocktailcraft.entity.Cocktail;

public class SearchViewModel {

    private final List<SearchViewModelListener> listeners = new ArrayList<>();
    private List<Cocktail> results = new ArrayList<>();
    private String errorMessage = "";

    public List<Cocktail>  getResults() {
        return results;
    }

    public void setResults(List<Cocktail> results) {
        this.results = results;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
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
