package org.example.cocktailcraft.use_case.favourite;

public class FavouriteInputData {

    private final String cocktailID;


    public FavouriteInputData(String cocktailID) {
        this.cocktailID = cocktailID;
    }

    String getCocktailID() {
        return cocktailID;
    }
}
