package org.example.cocktailcraft.search;

import org.example.cocktailcraft.app.AppBuilder;
import org.example.cocktailcraft.data_access.CocktailJsonReader;
import org.example.cocktailcraft.data_access.SearchCocktailDataAccessObject;
import org.example.cocktailcraft.entity.Cocktail;
import org.example.cocktailcraft.entity.Tags.BaseSpirit;
import org.example.cocktailcraft.interface_adapter.search.SearchController;
import org.example.cocktailcraft.interface_adapter.search.SearchPresenter;
import org.example.cocktailcraft.interface_adapter.search.SearchViewModel;
import org.example.cocktailcraft.use_case.search.SearchInteractor;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.SwingUtilities;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SearchCatalogTest {
    @Test
    void loadsLazilyAndCachesTheSuccessfulRead() throws IOException {
        AtomicInteger reads = new AtomicInteger();
        SearchCocktailDataAccessObject dao = new SearchCocktailDataAccessObject(new CocktailJsonReader() {
            @Override
            public List<Cocktail> readAll() throws IOException {
                reads.incrementAndGet();
                return super.readAll();
            }
        });
        assertEquals(0, reads.get());
        List<Cocktail> catalog = dao.getAllCocktails();
        assertEquals(663, catalog.size());
        assertSame(catalog, dao.getAllCocktails());
        assertEquals(1, reads.get());
        assertThrows(UnsupportedOperationException.class, catalog::clear);
    }

    @Test
    void failedReadIsNotCachedAndNextRequestRetries() throws IOException {
        AtomicInteger reads = new AtomicInteger();
        SearchCocktailDataAccessObject dao = new SearchCocktailDataAccessObject(new CocktailJsonReader() {
            @Override
            public List<Cocktail> readAll() throws IOException {
                if (reads.getAndIncrement() == 0) {
                    throw new IOException("Temporary failure");
                }
                return super.readAll();
            }
        });
        assertThrows(IOException.class, dao::getAllCocktails);
        assertEquals(663, dao.getAllCocktails().size());
        dao.getAllCocktails();
        assertEquals(2, reads.get());
    }

    @Test
    void searchesRealRecipesAndClearRestoresAllRecipes() {
        SearchViewModel model = new SearchViewModel();
        SearchController controller = new SearchController(new SearchInteractor(
                new SearchCocktailDataAccessObject(new CocktailJsonReader()), new SearchPresenter(model)));
        controller.execute("  dAiQuIrI ", Set.of(BaseSpirit.RUM));
        assertTrue(model.getState().getResults().stream().anyMatch(cocktail -> cocktail.getId().equals("daiquiri")));
        assertTrue(model.getState().getResults().stream().allMatch(cocktail ->
                cocktail.getName().toLowerCase(java.util.Locale.ROOT).contains("daiquiri")
                        && cocktail.getTags().getBaseSpirits().contains(BaseSpirit.RUM)));
        assertTrue(model.getState().isSearchPerformed());
        controller.clear();
        assertEquals(663, model.getState().getResults().size());
        assertFalse(model.getState().isSearchPerformed());
        assertEquals("", model.getState().getErrorMessage());
    }

    @Test
    void matchesAllFourSelectedSpiritsInRealCatalog() {
        SearchViewModel model = new SearchViewModel();
        SearchController controller = new SearchController(new SearchInteractor(
                new SearchCocktailDataAccessObject(new CocktailJsonReader()), new SearchPresenter(model)));
        Set<BaseSpirit> spirits = Set.of(BaseSpirit.GIN, BaseSpirit.VODKA, BaseSpirit.RUM, BaseSpirit.TEQUILA);
        controller.execute("long island", spirits);
        assertFalse(model.getState().getResults().isEmpty());
        assertTrue(model.getState().getResults().stream()
                .anyMatch(cocktail -> cocktail.getId().equals("long-island-ice-tea")));
        assertTrue(model.getState().getResults().stream()
                .allMatch(cocktail -> cocktail.getTags().getBaseSpirits().containsAll(spirits)));
    }

    @Test
    void searchWiringDoesNotAbortStartupWhenCatalogIsUnavailable() throws Exception {
        SwingUtilities.invokeAndWait(() -> assertDoesNotThrow(() -> new AppBuilder()
                .addSearchView()
                .addSearchUseCase(() -> {
                    throw new IOException("Unavailable");
                })));
    }
}
