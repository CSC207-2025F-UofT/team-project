package org.example.cocktailcraft.use_case.search;

import org.example.cocktailcraft.entity.Cocktail;
import org.example.cocktailcraft.entity.Tags;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class SearchInteractor implements SearchInputBoundary {

    private final SearchDataAccessInterface searchDataAccessObject;
    private final SearchOutputBoundary searchPresenter;

    public SearchInteractor(SearchDataAccessInterface searchDataAccessObject,
                            SearchOutputBoundary searchPresenter) {
        this.searchDataAccessObject = searchDataAccessObject;
        this.searchPresenter = searchPresenter;
    }
    @Override
    public void execute(SearchInputData searchInputData) {
        String name = searchInputData.getName().strip();
        Set<Tags.BaseSpirit> baseSpirits = searchInputData.getBaseSpirits();

        if ("".equals(searchInputData.getName().strip()) && searchInputData.getBaseSpirits().isEmpty()) {
           searchPresenter.prepareFailureView("Please enter a cocktail name or select a base spirit");
        }
        else {
            List<Cocktail> candidates = searchDataAccessObject.getAllCocktails();
            List<Cocktail> results = new ArrayList<>();
            for (Cocktail cocktail : candidates) {
                if (matchesName(cocktail, name) && matchesBaseSpirits(cocktail, baseSpirits)) {
                    results.add(cocktail);
                }
            }
            SearchOutputData outputData = new SearchOutputData(results);
            searchPresenter.prepareSuccessView(outputData);
        }

    }

    private boolean matchesName(Cocktail cocktail, String name) {
        if (name.isEmpty()) {
            return true;
        }
        else {
            return cocktail.getName().toLowerCase(Locale.ROOT).contains(name.toLowerCase(Locale.ROOT));
        }
    }

    private boolean matchesBaseSpirits(Cocktail cocktail, Set<Tags.BaseSpirit> baseSpirits) {
        if (baseSpirits.isEmpty()) {
            return true;
        }
        else {
            return cocktail.getTags().getBaseSpirits().containsAll(baseSpirits);
        }
    }
}
