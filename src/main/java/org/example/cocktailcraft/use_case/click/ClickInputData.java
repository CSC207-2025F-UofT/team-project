package org.example.cocktailcraft.use_case.click;

public class ClickInputData {

    private final String cocktailID;

    public ClickInputData(String cocktailID) {
        this.cocktailID = cocktailID;
    }

    String getCocktailID() {
        return cocktailID;
    }
}
