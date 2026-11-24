package org.example.cocktailcraft.use_case.customize;

import java.util.List;
import org.example.cocktailcraft.entity.RecipeIngredient;

public class CustomizeOutputData {
    private final String customRecipeId;
    private final String name;
    private final List<RecipeIngredient> ingredients;
    private final String instructions;

    public CustomizeOutputData(String customRecipeId, String name,
                              List<RecipeIngredient> ingredients) {
        this(customRecipeId, name, ingredients, "");
    }

    public CustomizeOutputData(String customRecipeId, String name,
                              List<RecipeIngredient> ingredients, String instructions) {
        this.customRecipeId = customRecipeId;
        this.name = name;
        this.ingredients = List.copyOf(ingredients);
        this.instructions = instructions == null ? "" : instructions;
    }

    public String getCustomRecipeId() {
        return customRecipeId;
    }

    public String getName() {
        return name;
    }

    public List<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public String getInstructions() {
        return instructions;
    }
}

