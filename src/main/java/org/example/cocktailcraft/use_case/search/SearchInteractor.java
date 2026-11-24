package org.example.cocktailcraft.use_case.search;

import org.example.cocktailcraft.entity.Cocktail;
import org.example.cocktailcraft.entity.Tags;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/** Validates the search criteria and matches both name and every selected spirit. */
public class SearchInteractor implements SearchInputBoundary {
    private final SearchDataAccessInterface searchDataAccessObject;
    private final SearchOutputBoundary searchPresenter;

    public SearchInteractor(SearchDataAccessInterface searchDataAccessObject,
                            SearchOutputBoundary searchPresenter) {
        this.searchDataAccessObject = searchDataAccessObject;
        this.searchPresenter = searchPresenter;
    }

    @Override
    public void clear() {
        try {
            final List<Cocktail> catalog = searchDataAccessObject.getAllCocktails();
            searchPresenter.prepareSuccessView(new SearchOutputData(catalog, false));
        } catch (IOException | UncheckedIOException exception) {
            searchPresenter.prepareFailureView("Unable to load the cocktail catalog. Please try again.");
        }
    }

    @Override
    public void execute(SearchInputData searchInputData) {
        final String name = searchInputData.getName().toLowerCase(Locale.ROOT);
        final Set<Tags.BaseSpirit> baseSpirits = searchInputData.getBaseSpirits();
        if (name.isEmpty() && baseSpirits.isEmpty()) {
            searchPresenter.prepareFailureView("Please enter a cocktail name or select a base spirit.");
            return;
        }

        final List<Cocktail> candidates;
        try {
            candidates = searchDataAccessObject.getAllCocktails();
        } catch (IOException | UncheckedIOException exception) {
            searchPresenter.prepareFailureView("Unable to load the cocktail catalog. Please try again.");
            return;
        }

        final List<Cocktail> results = new ArrayList<>();
        for (Cocktail cocktail : candidates) {
            if (cocktail.getName().toLowerCase(Locale.ROOT).contains(name)
                    && cocktail.getTags().getBaseSpirits().containsAll(baseSpirits)) {
                results.add(cocktail);
            }
        }
        searchPresenter.prepareSuccessView(new SearchOutputData(results));
    }
}
