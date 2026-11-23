package org.example.cocktailcraft.interface_adapter.click;

import java.util.ArrayList;
import java.util.List;

public class ClickState {
    private String cocktailId = "";
    private List<String> ingredients = new ArrayList<>();
    private String cocktailError;

    public String getCocktailId(){
        return cocktailId;
    }

    public String getCocktailError(){
        return cocktailError;
    }

    public void setCocktailId(String cocktailId){
        this.cocktailId = cocktailId;
    }

    public void setCocktailError(String cocktailError){
        this.cocktailError = cocktailError;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}
