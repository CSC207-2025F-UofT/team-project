package org.example.cocktailcraft.use_case.click;

import org.example.cocktailcraft.entity.RecipeIngredient;
import org.example.cocktailcraft.entity.Cocktail;

import java.util.ArrayList;
import java.util.List;

public class ClickInteractor implements ClickInputBoundary {

    private final ClickDataAccessInterface clickDataAccessObject;
    private final ClickOutputBoundary clickPresenter;

    public ClickInteractor(ClickDataAccessInterface clickDataAccessObject,
                           ClickOutputBoundary clickPresenter) {
        this.clickDataAccessObject = clickDataAccessObject;
        this.clickPresenter = clickPresenter;
    }

    @Override
    public void execute(ClickInputData clickInputData) {
        final String cocktailID = clickInputData.getCocktailID();
        if (!clickDataAccessObject.existsByID(cocktailID)) {
            clickPresenter.prepareFailView(cocktailID + ": Cocktail does not exist.");
        } else {
            final Cocktail selectedCocktail = clickDataAccessObject.get(cocktailID);
            final List<RecipeIngredient> ingredients = selectedCocktail.getIngredients();
            List<String> result = new ArrayList<>();
            for (RecipeIngredient ingredient : ingredients) {
                String cocktail = ingredient.getName() + ": "
                        + ingredient.getAmount() + " "
                        + ingredient.getUnit();
                result.add(cocktail);
            }

            final ClickOutputData clickOutputData =
                    new ClickOutputData(result, selectedCocktail.getInstructions());
            clickPresenter.prepareSuccessView(clickOutputData);
        }
    }
}

