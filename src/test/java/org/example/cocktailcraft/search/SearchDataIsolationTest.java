package org.example.cocktailcraft.search;

import org.example.cocktailcraft.data_access.SearchCocktailDataAccessObject;
import org.example.cocktailcraft.entity.Cocktail;
import org.example.cocktailcraft.entity.Tags;
import org.example.cocktailcraft.entity.Tags.BaseSpirit;
import org.example.cocktailcraft.use_case.search.SearchInputData;
import org.example.cocktailcraft.use_case.search.SearchInteractor;
import org.example.cocktailcraft.use_case.search.SearchOutputBoundary;
import org.example.cocktailcraft.use_case.search.SearchOutputData;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

class SearchDataIsolationTest {
    private final Cocktail gin = new Cocktail("gin", "Gin Fizz", List.of(), new Tags(Set.of(BaseSpirit.GIN)));
    private final Cocktail rum = new Cocktail("rum", "Daiquiri", List.of(), new Tags(Set.of(BaseSpirit.RUM)));

    @Test
    void laterSelectionChangesDoNotChangeAnExistingSearchRequest() {
        Set<BaseSpirit> selections = EnumSet.of(BaseSpirit.GIN);
        SearchInputData request = new SearchInputData("", selections);
        selections.clear();
        selections.add(BaseSpirit.RUM);
        AtomicReference<List<Cocktail>> result = new AtomicReference<>();
        SearchOutputBoundary presenter = new SearchOutputBoundary() {
            @Override
            public void prepareSuccessView(SearchOutputData outputData) {
                result.set(outputData.getResults());
            }

            @Override
            public void prepareFailureView(String errorMessage) {
                fail(errorMessage);
            }
        };
        new SearchInteractor(() -> List.of(gin, rum), presenter).execute(request);
        assertEquals(List.of(gin), result.get());
    }

    @Test
    void callerCannotChangeTheStoredCatalog() {
        List<Cocktail> source = new ArrayList<>(List.of(gin, rum));
        SearchCocktailDataAccessObject dao = new SearchCocktailDataAccessObject(source);
        source.clear();
        assertEquals(List.of(gin, rum), dao.getAllCocktails());
        assertThrows(UnsupportedOperationException.class, () -> dao.getAllCocktails().clear());
    }

    @Test
    void resultRemainsStableAfterItsSourceChanges() {
        List<Cocktail> source = new ArrayList<>(List.of(gin));
        SearchOutputData output = new SearchOutputData(source);
        source.clear();
        assertEquals(List.of(gin), output.getResults());
        assertThrows(UnsupportedOperationException.class, () -> output.getResults().clear());
    }
}
