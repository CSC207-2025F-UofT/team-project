package org.example.cocktailcraft.entity;

public class RecipeIngredient {
    private final Ingredient ingredient;
    private final String amount;
    private final String unit;

    public RecipeIngredient(Ingredient ingredient, String amount, String unit) {
        this.ingredient = ingredient;
        this.amount = amount;
        this.unit = unit;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public String getAmount() {
        return amount;
    }

    public String getUnit() {
        return unit;
    }
}
