package org.example.cocktailcraft.use_case.customize;

/** Initial input shape. TODO(Enhao): extend with the agreed use case contract. */
public class CustomizeInputData {
    private final String originalCocktailId;

    public CustomizeInputData(String originalCocktailId) {
        this.originalCocktailId = originalCocktailId;
    }

    public String getOriginalCocktailId() {
        return originalCocktailId;
    }
}
