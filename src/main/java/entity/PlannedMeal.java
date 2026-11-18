package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A recipe assigned to a meal slot, with a copied ingredient list and cooked flag.
 */
public class PlannedMeal {

    private final String recipeId;
    private final String recipeName;
    private final List<RecipeIngredient> ingredients;
    private boolean cooked;

    /**
     * Creates a planned meal that has not been cooked yet.
     *
     * @param recipeId the recipe id
     * @param recipeName the recipe name
     * @param ingredients a copy of the recipe ingredients
     */
    public PlannedMeal(String recipeId, String recipeName, List<RecipeIngredient> ingredients) {
        if (recipeId == null || recipeName == null) {
            throw new IllegalArgumentException("Recipe id and name cannot be null");
        }
        if (ingredients == null) {
            throw new IllegalArgumentException("Ingredients cannot be null");
        }
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.ingredients = new ArrayList<>(ingredients);
        this.cooked = false;
    }

    /**
     * Returns the recipe id.
     *
     * @return the recipe id
     */
    public String getRecipeId() {
        return recipeId;
    }

    /**
     * Returns the recipe name.
     *
     * @return the recipe name
     */
    public String getRecipeName() {
        return recipeName;
    }

    /**
     * Returns an unmodifiable view of the ingredients.
     *
     * @return the ingredients
     */
    public List<RecipeIngredient> getIngredients() {
        return Collections.unmodifiableList(ingredients);
    }

    /**
     * Returns whether this meal has already been cooked.
     *
     * @return true if cooked
     */
    public boolean isCooked() {
        return cooked;
    }

    /**
     * Marks this meal as cooked. Fails if it is already cooked.
     */
    public void markCooked() {
        if (cooked) {
            throw new IllegalStateException("Meal is already cooked");
        }
        cooked = true;
    }
}
