package org.example.cocktailcraft.search;

import org.example.cocktailcraft.entity.Cocktail;
import org.example.cocktailcraft.entity.Tags;
import org.example.cocktailcraft.entity.Tags.BaseSpirit;
import org.example.cocktailcraft.use_case.search.SearchDataAccessInterface;
import org.example.cocktailcraft.use_case.search.SearchInputData;
import org.example.cocktailcraft.use_case.search.SearchInteractor;
import org.example.cocktailcraft.use_case.search.SearchOutputBoundary;
import org.example.cocktailcraft.use_case.search.SearchOutputData;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SearchInteractorTest {
    private final Cocktail gin = cocktail("gin", "Gin Fizz", BaseSpirit.GIN);
    private final Cocktail rum = cocktail("rum", "Rum Fizz", BaseSpirit.RUM);
    private final Cocktail mixed = cocktail("mixed", "Long Island", BaseSpirit.GIN, BaseSpirit.RUM);
    private final List<Cocktail> catalog = List.of(gin, rum, mixed);
    private final RecordingPresenter presenter = new RecordingPresenter();

    @Test
    void matchesTrimmedPartialNameWithoutCaseSensitivity() {
        search("  FiZz  ", Set.of());
        assertEquals(List.of(gin, rum), presenter.results.getResults());
        assertNull(presenter.error);
        assertEquals(1, presenter.successes);
    }

    @Test
    void searchesBySpiritWithoutName() {
        search("", Set.of(BaseSpirit.GIN));
        assertEquals(List.of(gin, mixed), presenter.results.getResults());
    }

    @Test
    void requiresEverySelectedSpirit() {
        search("", Set.of(BaseSpirit.GIN, BaseSpirit.RUM));
        assertEquals(List.of(mixed), presenter.results.getResults());
    }

    @Test
    void combinesNameAndSpiritCriteria() {
        search("fizz", Set.of(BaseSpirit.RUM));
        assertEquals(List.of(rum), presenter.results.getResults());
    }

    @Test
    void noMatchesIsASuccessfulEmptyResult() {
        search("fizz", Set.of(BaseSpirit.TEQUILA));
        assertTrue(presenter.results.getResults().isEmpty());
        assertNull(presenter.error);
        assertEquals(1, presenter.successes);
    }

    @Test
    void rejectsBlankInputBeforeAccessingCatalog() {
        SearchInteractor interactor = new SearchInteractor(() -> {
            throw new AssertionError("Invalid input must not access the catalog");
        }, presenter);
        interactor.execute(new SearchInputData(" \t ", Set.of()));
        assertEquals("Please enter a cocktail name or select a base spirit.", presenter.error);
        assertNull(presenter.results);
        assertEquals(0, presenter.successes);
    }

    @Test
    void treatsNullCriteriaAsMissingInput() {
        search(null, null);
        assertEquals("Please enter a cocktail name or select a base spirit.", presenter.error);
        assertEquals(1, presenter.failures);
    }

    @Test
    void reportsCatalogFailureAndAllowsRetry() {
        AtomicInteger attempts = new AtomicInteger();
        SearchDataAccessInterface dao = () -> {
            if (attempts.getAndIncrement() == 0) {
                throw new IOException("Unavailable");
            }
            return catalog;
        };
        SearchInteractor interactor = new SearchInteractor(dao, presenter);
        interactor.execute(new SearchInputData("fizz", Set.of()));
        assertEquals("Unable to load the cocktail catalog. Please try again.", presenter.error);
        assertNull(presenter.results);
        interactor.execute(new SearchInputData("fizz", Set.of()));
        assertEquals(List.of(gin, rum), presenter.results.getResults());
        assertEquals(1, presenter.failures);
        assertEquals(1, presenter.successes);
        assertEquals(2, attempts.get());
    }

    @Test
    void reportsUncheckedIoFailure() {
        SearchInteractor interactor = new SearchInteractor(() -> {
            throw new UncheckedIOException(new IOException("Unavailable"));
        }, presenter);
        interactor.execute(new SearchInputData("gin", Set.of()));
        assertEquals("Unable to load the cocktail catalog. Please try again.", presenter.error);
        assertEquals(1, presenter.failures);
    }

    @Test
    void matchingDoesNotDependOnSystemLocale() {
        Locale original = Locale.getDefault();
        try {
            Locale.setDefault(Locale.forLanguageTag("tr-TR"));
            search("GIN", Set.of());
            assertEquals(List.of(gin), presenter.results.getResults());
        } finally {
            Locale.setDefault(original);
        }
    }

    @Test
    void matchesAllKeywordsAcrossConnectingWordsAndSymbols() {
        Cocktail symbol = cocktail("symbol", "Gin & Tonic", BaseSpirit.GIN);
        Cocktail word = cocktail("word", "Bright Orchard Gin and Tonic", BaseSpirit.GIN);
        Cocktail tonicOnly = cocktail("tonic", "Vodka Tonic", BaseSpirit.VODKA);
        List<Cocktail> recipes = List.of(symbol, word, tonicOnly, gin);
        SearchInteractor interactor = new SearchInteractor(() -> recipes, presenter);
        for (String query : List.of("gin tonic", "  GIN   TONIC  ", "tonic\tgin")) {
            interactor.execute(new SearchInputData(query, Set.of()));
            assertEquals(List.of(symbol, word), presenter.results.getResults(), query);
        }
    }

    @Test
    void keywordMatchingStillRequiresTheSelectedSpirit() {
        Cocktail ginTonic = cocktail("gin-tonic", "Gin & Tonic", BaseSpirit.GIN);
        SearchInteractor interactor = new SearchInteractor(() -> List.of(ginTonic), presenter);
        interactor.execute(new SearchInputData("gin tonic", Set.of(BaseSpirit.RUM)));
        assertTrue(presenter.results.getResults().isEmpty());
        interactor.execute(new SearchInputData("gin tonic", Set.of(BaseSpirit.GIN)));
        assertEquals(List.of(ginTonic), presenter.results.getResults());
    }

    private void search(String name, Set<BaseSpirit> spirits) {
        new SearchInteractor(() -> catalog, presenter).execute(new SearchInputData(name, spirits));
    }

    private static Cocktail cocktail(String id, String name, BaseSpirit... spirits) {
        return new Cocktail(id, name, List.of(), new Tags(Set.of(spirits)));
    }

    private static final class RecordingPresenter implements SearchOutputBoundary {
        private SearchOutputData results;
        private String error;
        private int successes;
        private int failures;

        @Override
        public void prepareSuccessView(SearchOutputData outputData) {
            results = outputData;
            error = null;
            successes++;
        }

        @Override
        public void prepareFailureView(String errorMessage) {
            error = errorMessage;
            failures++;
        }
    }
}
