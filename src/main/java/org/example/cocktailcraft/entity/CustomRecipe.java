package org.example.cocktailcraft.entity;
import java.util.List;

public class CustomRecipe {
    private final String id;
    private final String ownerId;
    private final String originalCocktailId;
    private final String name;
    private final List<RecipeIngredient> ingredients;
    private final String instructions;

    public CustomRecipe(String id, String ownerId,
                        String originalCocktailId, String name,
                        List<RecipeIngredient> ingredients) {
        this(id, ownerId, originalCocktailId, name, ingredients, "");
    }

    public CustomRecipe(String id, String ownerId,
                        String originalCocktailId, String name,
                        List<RecipeIngredient> ingredients, String instructions) {
        this.id = id;
        this.ownerId = ownerId;
        this.originalCocktailId = originalCocktailId;
        this.name = name;
        this.ingredients = List.copyOf(ingredients);
        this.instructions = instructions == null ? "" : instructions;
    }

    public String getId() {
        return id;
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
        // Gson leaves new fields null when loading older saved recipes.
        return instructions == null ? "" : instructions;
    }
}

