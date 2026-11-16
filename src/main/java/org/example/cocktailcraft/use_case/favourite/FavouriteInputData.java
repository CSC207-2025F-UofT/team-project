package org.example.cocktailcraft.use_case.favourite;

/** Initial input shape. TODO(Wenqian): extend with the agreed use case contract. */
public class FavouriteInputData {
    private final String cocktailId;

    public FavouriteInputData(String cocktailId) {
        this.cocktailId = cocktailId;
    }

    public String getCocktailId() {
        return cocktailId;
    }
}
