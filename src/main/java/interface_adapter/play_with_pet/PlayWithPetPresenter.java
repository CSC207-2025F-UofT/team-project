package interface_adapter.play_with_pet;

import interface_adapter.interact_with_pet.InteractWithPetState;
import interface_adapter.interact_with_pet.InteractWithPetViewModel;
import use_case.play_with_pet.PlayWithPetOutputBoundary;
import use_case.play_with_pet.PlayWithPetOutputData;

public class PlayWithPetPresenter
        implements PlayWithPetOutputBoundary {

    private final InteractWithPetViewModel viewModel;

    public PlayWithPetPresenter(InteractWithPetViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(PlayWithPetOutputData outputData) {
        InteractWithPetState state = viewModel.getState();

        state.setHappiness(outputData.getHappiness());
        state.setErrorMessage("");

        viewModel.notifyStateChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        viewModel.getState().setErrorMessage(errorMessage);
        viewModel.notifyStateChanged();
    }
}