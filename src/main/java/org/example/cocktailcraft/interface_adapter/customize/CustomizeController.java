package org.example.cocktailcraft.interface_adapter.customize;

import java.util.List;
import org.example.cocktailcraft.entity.RecipeIngredient;
import org.example.cocktailcraft.use_case.customize.CustomizeInputBoundary;
import org.example.cocktailcraft.use_case.customize.CustomizeInputData;

public class CustomizeController {
    private final CustomizeInputBoundary interactor;

    public CustomizeController(CustomizeInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(
            String customRecipeId,
            String ownerId,
            String originalCocktailId,
            String name,
            List<RecipeIngredient> ingredients) {

        execute(customRecipeId, ownerId, originalCocktailId, name, ingredients, "");
    }

    public void execute(
            String customRecipeId,
            String ownerId,
            String originalCocktailId,
            String name,
            List<RecipeIngredient> ingredients,
            String instructions) {

        CustomizeInputData inputData = new CustomizeInputData(
                customRecipeId,
                ownerId,
                originalCocktailId,
                name,
                ingredients,
                instructions);

        interactor.execute(inputData);
    }
}
