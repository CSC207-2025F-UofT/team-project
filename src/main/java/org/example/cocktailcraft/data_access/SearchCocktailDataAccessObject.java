package org.example.cocktailcraft.data_access;

import org.example.cocktailcraft.entity.Cocktail;
import org.example.cocktailcraft.use_case.search.SearchDataAccessInterface;

import java.io.IOException;
import java.util.List;

/** Loads the bundled catalog on demand and caches only a successful read. */
public class SearchCocktailDataAccessObject implements SearchDataAccessInterface {
    private final CocktailJsonReader reader;
    private List<Cocktail> cocktails;

    public SearchCocktailDataAccessObject(List<Cocktail> cocktails) {
        this.reader = null;
        this.cocktails = List.copyOf(cocktails);
    }

    public SearchCocktailDataAccessObject(CocktailJsonReader reader) {
        this.reader = reader;
    }

    @Override
    public List<Cocktail> getAllCocktails() throws IOException {
        if (cocktails == null) {
            cocktails = List.copyOf(reader.readAll());
        }
        return cocktails;
    }
}
