package org.example.cocktailcraft.use_case.click;
import java.util.List;

public class ClickOutputData {

    private final List<String> ingredients;

    public ClickOutputData(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getIngredients() {
        return ingredients;
    }
}
