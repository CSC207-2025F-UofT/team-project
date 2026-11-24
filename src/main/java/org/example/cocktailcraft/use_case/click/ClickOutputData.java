package org.example.cocktailcraft.use_case.click;
import java.util.List;

public class ClickOutputData {

    private final List<String> ingredients;
    private final String instructions;

    public ClickOutputData(List<String> ingredients) {
        this(ingredients, "");
    }

    public ClickOutputData(List<String> ingredients, String instructions) {
        this.ingredients = ingredients;
        this.instructions = instructions == null ? "" : instructions;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public String getInstructions() {
        return instructions;
    }
}
