package org.example.cocktailcraft.use_case.details;

/** Initial input shape. TODO(Wenqian): extend with the agreed use case contract. */
public class DetailsInputData {
    private final String cocktailId;

    public DetailsInputData(String cocktailId) {
        this.cocktailId = cocktailId;
    }

    public String getCocktailId() {
        return cocktailId;
    }
}
