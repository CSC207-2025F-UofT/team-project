package org.example.cocktailcraft.use_case.search;

import org.example.cocktailcraft.entity.Cocktail;

import java.util.List;

public class SearchOutputData {

    private final List<Cocktail> results;
    private final boolean searchPerformed;

    public SearchOutputData(List<Cocktail> results) {
        this(results, true);
    }

    public SearchOutputData(List<Cocktail> results, boolean searchPerformed) {
        this.results = List.copyOf(results);
        this.searchPerformed = searchPerformed;
    }

    public boolean isSearchPerformed() {
        return searchPerformed;
    }

    public List<Cocktail> getResults() {
        return this.results;
    }

}

