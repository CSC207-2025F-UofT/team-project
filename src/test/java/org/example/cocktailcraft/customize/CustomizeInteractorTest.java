package org.example.cocktailcraft.customize;

import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertSame;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.example.cocktailcraft.entity.CustomRecipe;
import org.example.cocktailcraft.entity.RecipeIngredient;
import org.example.cocktailcraft.use_case.customize.CustomizeDataAccessInterface;
import org.example.cocktailcraft.use_case.customize.CustomizeInputData;
import org.example.cocktailcraft.use_case.customize.CustomizeInteractor;
import org.example.cocktailcraft.use_case.customize.CustomizeOutputBoundary;
import org.example.cocktailcraft.use_case.customize.CustomizeOutputData;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CustomizeInteractorTest {
    @Test
    void createsCustomRecipe() {
        TestDataAccess dataAccess = new TestDataAccess();
        TestPresenter presenter = new TestPresenter();
        CustomizeInteractor interactor =
                new CustomizeInteractor(dataAccess, presenter);

        List<RecipeIngredient> ingredients = List.of(
                new RecipeIngredient("Sugar", "1", "tsp")
        );

        CustomizeInputData inputData = new CustomizeInputData(
                null,
                "user-001",
                "mojito",
                "My Mojito",
                ingredients,
                "1. Add sugar.\n2. Stir with ice."
        );

        interactor.execute(inputData);
        assertNotNull(dataAccess.savedRecipe);
        assertFalse(dataAccess.savedRecipe.getId().isBlank());
        assertEquals("user-001", dataAccess.savedRecipe.getOwnerId());
        assertEquals("mojito", dataAccess.savedRecipe.getOriginalCocktailId());
        assertEquals("My Mojito", dataAccess.savedRecipe.getName());
        assertEquals(ingredients, dataAccess.savedRecipe.getIngredients());
        assertEquals(inputData.getInstructions(), dataAccess.savedRecipe.getInstructions());

        assertNull(presenter.errorMessage);
        assertNotNull(presenter.outputData);
        assertEquals(dataAccess.savedRecipe.getId(), presenter.outputData.getCustomRecipeId());
        assertEquals("My Mojito", presenter.outputData.getName());
        assertEquals(ingredients, presenter.outputData.getIngredients());
        assertEquals(inputData.getInstructions(), presenter.outputData.getInstructions());
    }

    @Test
    void rejectsZeroQuantity() {
        TestDataAccess dataAccess = new TestDataAccess();
        TestPresenter presenter = new TestPresenter();
        CustomizeInteractor interactor = new CustomizeInteractor(dataAccess, presenter);

        CustomizeInputData inputData = new CustomizeInputData(
                null,
                "user-001",
                "mojito",
                "My Mojito",
                List.of(new RecipeIngredient("Sugar", "0", "tsp"))
        );

        interactor.execute(inputData);

        assertEquals("Quantities must be greater than 0.", presenter.errorMessage);
        assertNull(dataAccess.savedRecipe);
        assertNull(presenter.outputData);
    }

    @Test
    void editsOwnRecipe() {
        TestDataAccess dataAccess = new TestDataAccess();
        TestPresenter presenter = new TestPresenter();

        dataAccess.savedRecipe = new CustomRecipe(
                "custom-001",
                "user-001",
                "mojito",
                "My Mojito",
                List.of(new RecipeIngredient("Sugar", "2", "tsp")),
                "Add two teaspoons of sugar."
        );

        CustomizeInteractor interactor =
                new CustomizeInteractor(dataAccess, presenter);

        CustomizeInputData inputData = new CustomizeInputData(
                "custom-001",
                "user-001",
                "mojito",
                "Less Sweet Mojito",
                List.of(new RecipeIngredient("Sugar", "1", "tsp")),
                "Add one teaspoon of sugar, then stir."
        );

        interactor.execute(inputData);

        assertNull(presenter.errorMessage);
        assertNotNull(presenter.outputData);
        assertEquals("custom-001", dataAccess.savedRecipe.getId());
        assertEquals("user-001", dataAccess.savedRecipe.getOwnerId());
        assertEquals("mojito", dataAccess.savedRecipe.getOriginalCocktailId());
        assertEquals("Less Sweet Mojito", dataAccess.savedRecipe.getName());
        assertEquals("1", dataAccess.savedRecipe.getIngredients().get(0).getAmount());
        assertEquals("custom-001", presenter.outputData.getCustomRecipeId());
        assertEquals(inputData.getInstructions(), dataAccess.savedRecipe.getInstructions());
        assertEquals(inputData.getInstructions(), presenter.outputData.getInstructions());
    }

    @Test
    void rejectsEditingAnotherUsersRecipe() {
        TestDataAccess dataAccess = new TestDataAccess();
        TestPresenter presenter = new TestPresenter();

        CustomRecipe original = new CustomRecipe(
                "custom-001",
                "user-001",
                "mojito",
                "My Mojito",
                List.of(new RecipeIngredient("Sugar", "2", "tsp")),
                "Add two teaspoons of sugar."
        );
        dataAccess.savedRecipe = original;

        CustomizeInteractor interactor =
                new CustomizeInteractor(dataAccess, presenter);

        CustomizeInputData inputData = new CustomizeInputData(
                "custom-001",
                "user-002",
                "mojito",
                "Changed Recipe",
                List.of(new RecipeIngredient("Sugar", "1", "tsp")),
                "Add one teaspoon of sugar, then stir."
        );

        interactor.execute(inputData);

        assertEquals("You can only edit your own recipes.", presenter.errorMessage);
        assertNull(presenter.outputData);
        assertSame(original, dataAccess.savedRecipe);
        assertEquals("Add two teaspoons of sugar.", dataAccess.savedRecipe.getInstructions());
    }

    @Test
    void reportsSaveFailure() {
        TestDataAccess dataAccess = new TestDataAccess();
        dataAccess.failOnSave = true;
        TestPresenter presenter = new TestPresenter();

        CustomizeInteractor interactor =
                new CustomizeInteractor(dataAccess, presenter);

        CustomizeInputData inputData = new CustomizeInputData(
                null,
                "user-001",
                "mojito",
                "My Mojito",
                List.of(new RecipeIngredient("Sugar", "1", "tsp")),
                "Add one teaspoon of sugar, then stir."
        );

        interactor.execute(inputData);

        assertEquals(
                "Unable to load or save the recipe. Please try again.",
                presenter.errorMessage
        );
        assertNull(dataAccess.savedRecipe);
        assertNull(presenter.outputData);
    }

    private static class TestDataAccess implements CustomizeDataAccessInterface {
        private CustomRecipe savedRecipe;
        private boolean failOnSave;

        @Override
        public Optional<CustomRecipe> findById(String customRecipeId) {
            if (savedRecipe != null
                    && savedRecipe.getId().equals(customRecipeId)) {
                return Optional.of(savedRecipe);
            }
            return Optional.empty();
        }

        @Override
        public void save(CustomRecipe recipe) throws IOException {
            if (failOnSave) {
                throw new IOException("Simulated save failure.");
            }
            savedRecipe = recipe;
        }
    }

    private static class TestPresenter implements CustomizeOutputBoundary {
        private CustomizeOutputData outputData;
        private String errorMessage;

        @Override
        public void prepareSuccessView(CustomizeOutputData data) {
            outputData = data;
        }

        @Override
        public void prepareFailView(String message) {
            errorMessage = message;
        }
    }
}
