package org.example.cocktailcraft.interface_adapter.customize;

import java.util.List;
import org.example.cocktailcraft.entity.RecipeIngredient;

public class CustomizeState {
    private String customRecipeId;
    private String originalCocktailId;
    private String name = "";
    private List<RecipeIngredient> ingredients = List.of();
    private String errorMessage;
    private boolean saved;

    public String getCustomRecipeId(){
        return customRecipeId;
    }

    public void setCustomRecipeId(String customRecipeId) {
        this.customRecipeId = customRecipeId;
    }

    public String getOriginalCocktailId() {
        return originalCocktailId;
    }

    public void setOriginalCocktailId(String originalCocktailId) {
        this.originalCocktailId = originalCocktailId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<RecipeIngredient> ingredients) {
        this.ingredients = List.copyOf(ingredients);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }
}
