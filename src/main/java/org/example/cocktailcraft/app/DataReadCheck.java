package org.example.cocktailcraft.app;

import org.example.cocktailcraft.data_access.CocktailJsonReader;
import org.example.cocktailcraft.entity.Cocktail;
import org.example.cocktailcraft.entity.RecipeIngredient;

import java.io.IOException;
import java.util.List;

public class DataReadCheck {
    public static void main(String[] args) throws IOException {
        CocktailJsonReader reader = new CocktailJsonReader();
        List<Cocktail> cocktails = reader.readAll();
        System.out.println("Loaded cocktails: " + cocktails.size());
        Cocktail cocktail = cocktails.stream()
                .filter(item -> "Daiquiri".equals(item.getName()))
                .findFirst()
                .orElseThrow(() -> new IOException("Daiquiri missing from dataset"));

        System.out.println("Cocktail ID: " + cocktail.getId());
        System.out.println("Cocktail: " + cocktail.getName());

        for (RecipeIngredient ingredient : cocktail.getIngredients()) {
            System.out.println(
                    ingredient.getName() + ": "
                            + ingredient.getAmount() + " "
                            + ingredient.getUnit()
            );
        }
    }
}
