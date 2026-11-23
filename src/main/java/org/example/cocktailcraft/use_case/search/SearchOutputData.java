package org.example.cocktailcraft.use_case.search;

import org.example.cocktailcraft.entity.Cocktail;

import java.util.List;

public class SearchOutputData {

    private final List<Cocktail> results;

    public SearchOutputData(List<Cocktail>  results) {
        this.results = results;
    }

    public List<Cocktail> getResults() {
        return this.results;
    }

}

