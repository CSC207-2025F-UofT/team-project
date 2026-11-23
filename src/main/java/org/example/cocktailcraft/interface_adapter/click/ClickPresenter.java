package org.example.cocktailcraft.interface_adapter.click;

import org.example.cocktailcraft.use_case.click.ClickOutputBoundary;
import org.example.cocktailcraft.use_case.click.ClickOutputData;

public class ClickPresenter implements ClickOutputBoundary {
    private final ClickViewModel clickViewModel;
    public ClickPresenter(ClickViewModel clickViewModel){
        this.clickViewModel = clickViewModel;
    }

    public void prepareSuccessView(ClickOutputData outputData){
        ClickState state = clickViewModel.getState();

        state.setIngredients(outputData.getIngredients());
        state.setCocktailError(null);

        clickViewModel.setState(state);
        clickViewModel.notifyStateChanged();

    }

    public void prepareFailView(String errorMessage){
        ClickState state = clickViewModel.getState();

        state.setCocktailError(errorMessage);

        clickViewModel.setState(state);
        clickViewModel.notifyStateChanged();
    }
}
