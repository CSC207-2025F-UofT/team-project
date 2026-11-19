package org.example.cocktailcraft.interface_adapter.click;

import org.example.cocktailcraft.entity.Cocktail;

public class ClickState {
    private String cocktailId = "";
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
}
