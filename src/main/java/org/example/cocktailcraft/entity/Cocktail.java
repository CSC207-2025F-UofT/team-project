package org.example.cocktailcraft.entity;

import java.util.List;

public class Cocktail {
    private final String id;
    private final String name;
    private final List<RecipeIngredient> ingredients;
    private final Tags tags;
    private final String instructions;

    public Cocktail(String id, String name,
                    List<RecipeIngredient> ingredients, Tags tags) {
        this(id, name, ingredients, tags, "");
    }

    public Cocktail(String id, String name,
                    List<RecipeIngredient> ingredients, Tags tags, String instructions) {
        this.id = id;
        this.name = name;
        this.ingredients = List.copyOf(ingredients);
        this.tags = tags;
        this.instructions = instructions == null ? "" : instructions;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public Tags getTags() {
        return tags;
    }

    public String getInstructions() {
        return instructions;
    }
}
