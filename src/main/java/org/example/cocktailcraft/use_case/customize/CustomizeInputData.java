package org.example.cocktailcraft.use_case.customize;
import java.util.List;
import org.example.cocktailcraft.entity.RecipeIngredient;

public class CustomizeInputData {
    private final String customRecipeId;
    private final String ownerId;
    private final String originalCocktailId;
    private final String name;
    private final List<RecipeIngredient> ingredients;
    private final String instructions;

    public CustomizeInputData(String customRecipeId, String ownerId,
                              String originalCocktailId, String name,
                              List<RecipeIngredient> ingredients) {
        this(customRecipeId, ownerId, originalCocktailId, name, ingredients, "");
    }

    public CustomizeInputData(String customRecipeId, String ownerId,
                              String originalCocktailId, String name,
                              List<RecipeIngredient> ingredients, String instructions) {
        this.customRecipeId = customRecipeId;
        this.ownerId = ownerId;
        this.originalCocktailId = originalCocktailId;
        this.name = name;
        this.ingredients = List.copyOf(ingredients);
        this.instructions = instructions == null ? "" : instructions;
    }

    public String getCustomRecipeId() {
        return customRecipeId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getOriginalCocktailId() {
        return originalCocktailId;
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

