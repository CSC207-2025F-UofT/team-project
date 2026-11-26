package org.example.cocktailcraft.search;

import org.example.cocktailcraft.data_access.CocktailJsonReader;
import org.example.cocktailcraft.data_access.SearchCocktailDataAccessObject;
import org.example.cocktailcraft.entity.Cocktail;
import org.example.cocktailcraft.entity.Tags;
import org.example.cocktailcraft.entity.Tags.AlcoholRange;
import org.example.cocktailcraft.entity.Tags.BaseSpirit;
import org.example.cocktailcraft.entity.Tags.Flavor;
import org.example.cocktailcraft.interface_adapter.search.SearchController;
import org.example.cocktailcraft.interface_adapter.search.SearchPresenter;
import org.example.cocktailcraft.interface_adapter.search.SearchViewModel;
import org.example.cocktailcraft.use_case.search.SearchInputData;
import org.example.cocktailcraft.use_case.search.SearchInteractor;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SearchFilterTest {
    private final Cocktail sweet = recipe("Sweet Gin", Set.of(BaseSpirit.GIN), Set.of(Flavor.SWEET), 9.99);
    private final Cocktail bitter = recipe("Bitter Gin", Set.of(BaseSpirit.GIN), Set.of(Flavor.BITTER), 20.0);
    private final Cocktail fruity = recipe("Fruity Rum", Set.of(BaseSpirit.RUM), Set.of(Flavor.FRUITY_TROPICAL), 10.0);
    private final Cocktail zero = recipe("Coffee Zero", Set.of(), Set.of(Flavor.COFFEE_NUTTY), 0.0);
    private final Cocktail unknown = recipe("Unknown ABV", Set.of(), Set.of(), null);
    private final List<Cocktail> catalog = List.of(sweet, bitter, fruity, zero, unknown);
    private final SearchViewModel model = new SearchViewModel();
    private final SearchInteractor interactor = new SearchInteractor(() -> catalog, new SearchPresenter(model));
    private final SearchController controller = new SearchController(interactor);

    @Test
    void flavorAloneIsValidAndMultipleFlavorsMatchAnySelectedFamily() {
        controller.execute("", Set.of(), Set.of(Flavor.SWEET, Flavor.FRUITY_TROPICAL), Set.of());
        assertEquals(List.of(sweet, fruity), model.getState().getResults());
        assertEquals("", model.getState().getErrorMessage());
    }

    @Test
    void alcoholAloneIsValidAndMultipleRangesMatchAnySelectedRange() {
        controller.execute("", Set.of(), Set.of(), Set.of(AlcoholRange.LOW, AlcoholRange.HIGH));
        assertEquals(List.of(sweet, bitter), model.getState().getResults());
        controller.execute("", Set.of(), Set.of(), Set.of(AlcoholRange.MEDIUM));
        assertEquals(List.of(fruity), model.getState().getResults());
    }

    @Test
    void zeroAndUnknownAbvAreDistinctSelections() {
        controller.execute("", Set.of(), Set.of(), Set.of(AlcoholRange.ZERO));
        assertEquals(List.of(zero), model.getState().getResults());
        controller.execute("", Set.of(), Set.of(), Set.of(AlcoholRange.UNKNOWN));
        assertEquals(List.of(unknown), model.getState().getResults());
    }

    @Test
    void allFourCriteriaMustMatchAndLegacyOverloadStillWorks() {
        controller.execute("GIN", Set.of(BaseSpirit.GIN), Set.of(Flavor.BITTER), Set.of(AlcoholRange.HIGH));
        assertEquals(List.of(bitter), model.getState().getResults());
        controller.execute("gin", Set.of(BaseSpirit.GIN), Set.of(Flavor.BITTER), Set.of(AlcoholRange.LOW));
        assertTrue(model.getState().getResults().isEmpty());
        assertEquals("", model.getState().getErrorMessage());
        controller.execute("gin", Set.of(BaseSpirit.GIN));
        assertEquals(List.of(sweet, bitter), model.getState().getResults());
    }

    @Test
    void multipleSpiritsStillRequireAllWhileFlavorsMatchAny() {
        Cocktail mixed = recipe("Mixed", Set.of(BaseSpirit.GIN, BaseSpirit.RUM), Set.of(Flavor.SWEET), 15.0);
        SearchController mixedController = new SearchController(new SearchInteractor(
                () -> List.of(sweet, fruity, mixed), new SearchPresenter(model)));
        mixedController.execute("", Set.of(BaseSpirit.GIN, BaseSpirit.RUM),
                Set.of(Flavor.SWEET, Flavor.BITTER), Set.of(AlcoholRange.MEDIUM));
        assertEquals(List.of(mixed), model.getState().getResults());
    }

    @Test
    void nullSelectionsAreEmptyAndNoCriteriaDoesNotReadCatalog() {
        SearchController invalidController = new SearchController(new SearchInteractor(() -> {
            throw new AssertionError("No criteria must not load catalog");
        }, new SearchPresenter(model)));
        invalidController.execute(null, null, null, null);
        assertEquals("Please enter a cocktail name or select a filter.", model.getState().getErrorMessage());
    }

    @Test
    void newInputSelectionsAreDefensiveCopies() {
        Set<Flavor> flavors = EnumSet.of(Flavor.BITTER);
        Set<AlcoholRange> ranges = EnumSet.of(AlcoholRange.HIGH);
        SearchInputData request = new SearchInputData("", Set.of(), flavors, ranges);
        flavors.clear();
        flavors.add(Flavor.SWEET);
        ranges.clear();
        ranges.add(AlcoholRange.LOW);
        interactor.execute(request);
        assertEquals(List.of(bitter), model.getState().getResults());
    }

    @Test
    void filtersRealCatalogAndClearRestoresRecipesWithoutFlavorOrAbvMetadata() {
        SearchController real = new SearchController(new SearchInteractor(
                new SearchCocktailDataAccessObject(new CocktailJsonReader()), new SearchPresenter(model)));
        real.execute("negroni", Set.of(BaseSpirit.GIN), Set.of(Flavor.BITTER), Set.of(AlcoholRange.HIGH));
        assertTrue(model.getState().getResults().stream().anyMatch(cocktail -> cocktail.getId().equals("negroni")));
        assertTrue(model.getState().getResults().stream().allMatch(cocktail ->
                cocktail.getTags().getFlavors().contains(Flavor.BITTER)
                        && cocktail.getAlcoholRange() == AlcoholRange.HIGH
                        && cocktail.getTags().getBaseSpirits().contains(BaseSpirit.GIN)));
        real.clear();
        assertEquals(663, model.getState().getResults().size());
    }

    private static Cocktail recipe(String name, Set<BaseSpirit> spirits, Set<Flavor> flavors, Double abv) {
        return new Cocktail(name, name, List.of(), new Tags(spirits, flavors), "", abv);
    }
}
