package org.example.cocktailcraft.data_access;

import org.example.cocktailcraft.entity.Cocktail;
import org.example.cocktailcraft.use_case.search.SearchDataAccessInterface;

import java.util.List;

public class SearchCocktailDataAccessObject implements SearchDataAccessInterface {
    private final List<Cocktail> cocktails;

    public SearchCocktailDataAccessObject(List<Cocktail> cocktails) {
        this.cocktails = List.copyOf(cocktails);
    }

    @Override
    public List<Cocktail> getAllCocktails() {
        return cocktails;
    }
}
