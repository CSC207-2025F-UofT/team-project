package org.example.cocktailcraft.entity;
import java.util.List;

public class CustomRecipe {
    private final String id;
    private final String ownerId;
    private final String originalCocktailId;
    private final String name;
    private final List<RecipeIngredient> ingredients;

    public CustomRecipe(String id, String ownerId,
                        String originalCocktailId, String name,
                        List<RecipeIngredient> ingredients) {
        this.id = id;
        this.ownerId = ownerId;
        this.originalCocktailId = originalCocktailId;
        this.name = name;
        this.ingredients = List.copyOf(ingredients);
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
}

