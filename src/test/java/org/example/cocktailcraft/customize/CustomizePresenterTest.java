package org.example.cocktailcraft.customize;

import java.util.ArrayList;
import java.util.List;

import org.example.cocktailcraft.entity.RecipeIngredient;
import org.example.cocktailcraft.interface_adapter.customize.CustomizePresenter;
import org.example.cocktailcraft.interface_adapter.customize.CustomizeState;
import org.example.cocktailcraft.interface_adapter.customize.CustomizeViewModel;
import org.example.cocktailcraft.use_case.customize.CustomizeOutputData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class CustomizePresenterTest {

    @Test
    void updatesStateAndNotifiesOnSuccess() {
        CustomizeViewModel viewModel = new CustomizeViewModel();
        viewModel.getState().setErrorMessage("Previous error.");
        viewModel.getState().setOriginalCocktailId("mojito");

        List<String> notifiedIds = new ArrayList<>();
        viewModel.addListener(() ->
                notifiedIds.add(viewModel.getState().getCustomRecipeId()));

        CustomizePresenter presenter = new CustomizePresenter(viewModel);

        List<RecipeIngredient> ingredients =
                List.of(new RecipeIngredient("Sugar", "1", "tsp"));

        CustomizeOutputData outputData = new CustomizeOutputData(
                "custom-001",
                "My Mojito",
                ingredients
        );

        presenter.prepareSuccessView(outputData);

        CustomizeState state = viewModel.getState();

        assertEquals("custom-001", state.getCustomRecipeId());
        assertEquals("My Mojito", state.getName());
        assertEquals(ingredients, state.getIngredients());
        assertEquals("mojito", state.getOriginalCocktailId());
        assertNull(state.getErrorMessage());
        assertTrue(state.isSaved());
        assertEquals(List.of("custom-001"), notifiedIds);
    }

    @Test
    void preservesInputAndNotifiesOnFailure() {
        CustomizeViewModel viewModel = new CustomizeViewModel();

        List<RecipeIngredient> ingredients =
                List.of(new RecipeIngredient("Sugar", "1", "tsp"));

        CustomizeState state = viewModel.getState();
        state.setCustomRecipeId("custom-001");
        state.setOriginalCocktailId("mojito");
        state.setName("My Mojito");
        state.setIngredients(ingredients);
        state.setSaved(true);

        List<String> notifiedErrors = new ArrayList<>();
        viewModel.addListener(() ->
                notifiedErrors.add(viewModel.getState().getErrorMessage()));

        CustomizePresenter presenter = new CustomizePresenter(viewModel);
        String message = "Unable to save the recipe.";

        presenter.prepareFailView(message);

        assertEquals(message, state.getErrorMessage());
        assertFalse(state.isSaved());

        assertEquals("custom-001", state.getCustomRecipeId());
        assertEquals("mojito", state.getOriginalCocktailId());
        assertEquals("My Mojito", state.getName());
        assertEquals(ingredients, state.getIngredients());

        assertEquals(List.of(message), notifiedErrors);
    }
}