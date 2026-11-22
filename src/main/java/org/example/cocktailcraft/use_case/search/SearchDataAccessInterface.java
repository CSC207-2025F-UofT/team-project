package org.example.cocktailcraft.use_case.search;

import org.example.cocktailcraft.entity.Cocktail;

import java.util.List;

public interface SearchDataAccessInterface {
    List<Cocktail> getAllCocktails();
}
