package org.example.cocktailcraft.use_case.search;

import org.example.cocktailcraft.entity.Cocktail;
import org.example.cocktailcraft.entity.Tags;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/** Combines name, all selected spirits, any selected flavor and any selected ABV range. */
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
        final Set<Tags.Flavor> flavors = searchInputData.getFlavors();
        final Set<Tags.AlcoholRange> alcoholRanges = searchInputData.getAlcoholRanges();
        if (name.isEmpty() && baseSpirits.isEmpty() && flavors.isEmpty() && alcoholRanges.isEmpty()) {
            searchPresenter.prepareFailureView("Please enter a cocktail name or select a filter.");
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
            if (matchesName(cocktail, name)
                    && cocktail.getTags().getBaseSpirits().containsAll(baseSpirits)
                    && (flavors.isEmpty() || !Collections.disjoint(cocktail.getTags().getFlavors(), flavors))
                    && (alcoholRanges.isEmpty() || alcoholRanges.contains(cocktail.getAlcoholRange()))) {
                results.add(cocktail);
            }
        }
        searchPresenter.prepareSuccessView(new SearchOutputData(results));
    }

    private boolean matchesName(Cocktail cocktail, String query) {
        final String cocktailName = cocktail.getName().toLowerCase(Locale.ROOT);
        for (String keyword : query.split("\\s+")) {
            if (!cocktailName.contains(keyword)) {
                return false;
            }
        }
        return true;
    }

}
