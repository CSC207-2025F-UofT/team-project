package org.example.cocktailcraft.click;

import org.example.cocktailcraft.entity.Cocktail;
import org.example.cocktailcraft.entity.RecipeIngredient;
import org.example.cocktailcraft.use_case.click.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ClickInteractorTest {

    private FakeClickDataAccessObject dataAccess;
    private FakeClickPresenter presenter;
    private ClickInteractor interactor;

    @BeforeEach
    void setUp() {
        dataAccess = new FakeClickDataAccessObject();
        presenter = new FakeClickPresenter();
        interactor = new ClickInteractor(dataAccess, presenter);
    }

    @Test
    void executeNonExistingCocktail() {
        interactor.execute(new ClickInputData("invalid"));

        assertEquals(
                "invalid: Cocktail does not exist.",
                presenter.errorMessage
        );
        assertNull(presenter.outputData);
        assertFalse(dataAccess.getCalled);
    }

    @Test
    void executeExistingCocktail() {
        List<RecipeIngredient> ingredients = Arrays.asList(
                new RecipeIngredient("Vodka", "45", "ml"),
                new RecipeIngredient("Lime Juice", "20", "ml"),
                new RecipeIngredient("Sugar", "1", "tsp")
        );

        Cocktail cocktail = new Cocktail(
                "1",
                "Moscow Mule",
                ingredients,
                null,
                "Mix all ingredients."
        );

        dataAccess.addCocktail(cocktail);

        interactor.execute(new ClickInputData("1"));

        assertNotNull(presenter.outputData);
        assertNull(presenter.errorMessage);
        assertEquals(
                Arrays.asList(
                        "Vodka: 45 ml",
                        "Lime Juice: 20 ml",
                        "Sugar: 1 tsp"
                ),
                presenter.outputData.getIngredients()
        );
        assertEquals(
                "Mix all ingredients.",
                presenter.outputData.getInstructions()
        );
        assertTrue(dataAccess.getCalled);
    }

    @Test
    void executeCocktailWithNoIngredients() {
        Cocktail cocktail = new Cocktail(
                "2",
                "Simple Cocktail",
                Collections.emptyList(),
                null,
                "Serve chilled."
        );

        dataAccess.addCocktail(cocktail);

        interactor.execute(new ClickInputData("2"));

        assertNotNull(presenter.outputData);
        assertTrue(presenter.outputData.getIngredients().isEmpty());
        assertEquals(
                "Serve chilled.",
                presenter.outputData.getInstructions()
        );
    }

    @Test
    void executeCocktailWithEmptyInstructions() {
        Cocktail cocktail = new Cocktail(
                "3",
                "Basic Cocktail",
                Collections.singletonList(
                        new RecipeIngredient("Gin", "30", "ml")
                ),
                null
        );

        dataAccess.addCocktail(cocktail);

        interactor.execute(new ClickInputData("3"));

        assertNotNull(presenter.outputData);
        assertEquals(
                "Gin: 30 ml",
                presenter.outputData.getIngredients().get(0)
        );
        assertEquals("", presenter.outputData.getInstructions());
    }

    private static class FakeClickDataAccessObject
            implements ClickDataAccessInterface {

        private final Map<String, Cocktail> cocktails = new HashMap<>();
        private boolean getCalled = false;

        void addCocktail(Cocktail cocktail) {
            cocktails.put(cocktail.getId(), cocktail);
        }

        @Override
        public Cocktail get(String cocktailId) {
            getCalled = true;
            return cocktails.get(cocktailId);
        }

        @Override
        public boolean existsByID(String cocktailId) {
            return cocktails.containsKey(cocktailId);
        }
    }

    private static class FakeClickPresenter
            implements ClickOutputBoundary {

        private ClickOutputData outputData;
        private String errorMessage;

        @Override
        public void prepareSuccessView(ClickOutputData outputData) {
            this.outputData = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }
}
