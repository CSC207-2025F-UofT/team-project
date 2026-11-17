package org.example.cocktailcraft.entity;

public class RecipeIngredient {
    private final String name;
    private final String amount;
    private final String unit;

    public RecipeIngredient(String name, String amount, String unit) {
        this.name = name;
        this.amount = amount;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public String getAmount() {
        return amount;
    }

    public String getUnit() {
        return unit;
    }
}
