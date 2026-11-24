package org.example.cocktailcraft.interface_adapter.search;

import java.util.List;
import org.example.cocktailcraft.entity.Cocktail;

/** The display state for searching and returning to catalog browsing. */
public class SearchState {
    private List<Cocktail> results = List.of();
    private String errorMessage = "";
    private boolean searchPerformed;

    public List<Cocktail> getResults() {
        return results;
    }

    public void setResults(List<Cocktail> results) {
        this.results = List.copyOf(results);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isSearchPerformed() {
        return searchPerformed;
    }

    public void setSearchPerformed(boolean searchPerformed) {
        this.searchPerformed = searchPerformed;
    }
}
