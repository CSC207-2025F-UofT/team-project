package org.example.cocktailcraft.entity;

public class RecipeIngredient {
    private final String ingredient;
    private final String amount;
    private final String unit;

    public RecipeIngredient(String ingredient, String amount, String unit) {
        this.ingredient = ingredient;
        this.amount = amount;
        this.unit = unit;
    }

    public String getIngredient() {
        return ingredient;
    }

    public String getAmount() {
        return amount;
    }

    public String getUnit() {
        return unit;
    }
}
