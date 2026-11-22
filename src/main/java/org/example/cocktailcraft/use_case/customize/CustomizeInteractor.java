package org.example.cocktailcraft.use_case.customize;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.example.cocktailcraft.entity.CustomRecipe;
import org.example.cocktailcraft.entity.RecipeIngredient;

public class CustomizeInteractor implements CustomizeInputBoundary {
    private final CustomizeDataAccessInterface dataAccess;
    private final CustomizeOutputBoundary presenter;

    public CustomizeInteractor(CustomizeDataAccessInterface dataAccess,
                               CustomizeOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(CustomizeInputData inputData) {
        String error = validateInput(inputData);
        if (error != null) {
            presenter.prepareFailView(error);
            return;
        }
        String recipeId = inputData.getCustomRecipeId();
        String originalId = inputData.getOriginalCocktailId();
        CustomRecipe recipe;

        try {
            if (recipeId == null) {
                recipeId = UUID.randomUUID().toString();
                if (isBlank(originalId)) {
                    presenter.prepareFailView("Please select an original recipe.");
                    return;
                }
            } else {
                Optional<CustomRecipe> existing = dataAccess.findById(recipeId);
                if (existing.isEmpty()) {
                    presenter.prepareFailView("Custom recipe not found.");
                    return;
                }

                CustomRecipe savedRecipe = existing.get();
                if (!savedRecipe.getOwnerId().equals(inputData.getOwnerId())) {
                    presenter.prepareFailView("You can only edit your own recipes.");
                    return;
                }

                originalId = savedRecipe.getOriginalCocktailId();
            }

            recipe = new CustomRecipe(
                    recipeId,
                    inputData.getOwnerId(),
                    originalId,
                    inputData.getName().trim(),
                    inputData.getIngredients()
            );
            dataAccess.save(recipe);
        } catch (IOException exception) {
            presenter.prepareFailView(
                    "Unable to load or save the recipe. Please try again."
            );
            return;
        }

        CustomizeOutputData outputData = new CustomizeOutputData(
                recipe.getId(),
                recipe.getName(),
                recipe.getIngredients()
        );
        presenter.prepareSuccessView(outputData);
    }

    private String validateInput(CustomizeInputData inputData) {
        if (inputData == null) {
            return "No recipe data was provided.";
        }
        if (isBlank(inputData.getOwnerId())) {
            return "Please log in before saving a recipe.";
        }
        if (isBlank(inputData.getName())) {
            return "Please enter a recipe name.";
        }
        if (inputData.getIngredients().isEmpty()) {
            return "Please add at least one ingredient.";
        }

        for (RecipeIngredient ingredient : inputData.getIngredients()) {
            if (isBlank(ingredient.getName())) {
                return "Please enter an ingredient name.";
            }
            if (isBlank(ingredient.getUnit())) {
                return "Please enter a unit for each ingredient.";
            }
            if (isBlank(ingredient.getAmount())) {
                return "Please enter a quantity for each ingredient.";
            }

            try {
                BigDecimal amount = new BigDecimal(ingredient.getAmount().trim());
                if (amount.signum() <= 0) {
                    return "Quantities must be greater than 0.";
                }
            } catch (NumberFormatException exception) {
                return "Quantities must be valid numbers.";
            }
        }

        return null;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}

