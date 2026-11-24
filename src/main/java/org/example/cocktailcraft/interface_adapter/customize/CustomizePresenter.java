package org.example.cocktailcraft.interface_adapter.customize;

import org.example.cocktailcraft.use_case.customize.CustomizeOutputBoundary;
import org.example.cocktailcraft.use_case.customize.CustomizeOutputData;

public class CustomizePresenter implements CustomizeOutputBoundary {
    private final CustomizeViewModel viewModel;

    public CustomizePresenter(CustomizeViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(CustomizeOutputData outputData) {
        CustomizeState state = viewModel.getState();

        state.setCustomRecipeId(outputData.getCustomRecipeId());
        state.setName(outputData.getName());
        state.setIngredients(outputData.getIngredients());
        state.setInstructions(outputData.getInstructions());
        state.setErrorMessage(null);
        state.setSaved(true);

        viewModel.setState(state);
        viewModel.notifyStateChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        CustomizeState state = viewModel.getState();

        state.setErrorMessage(errorMessage);
        state.setSaved(false);

        viewModel.setState(state);
        viewModel.notifyStateChanged();
    }
}
