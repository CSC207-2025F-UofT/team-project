package interface_adapter.adopt_pet;

import interface_adapter.ViewManagerModel;
import interface_adapter.interact_with_pet.InteractWithPetState;
import interface_adapter.interact_with_pet.InteractWithPetViewModel;
import use_case.adopt_pet.AdoptPetOutputBoundary;
import use_case.adopt_pet.AdoptPetOutputData;

public class AdoptPetPresenter implements AdoptPetOutputBoundary {

    private final AdoptPetViewModel adoptPetViewModel;
    private final InteractWithPetViewModel interactWithPetViewModel;
    private final ViewManagerModel viewManagerModel;

    public AdoptPetPresenter(
            AdoptPetViewModel adoptPetViewModel,
            InteractWithPetViewModel interactWithPetViewModel,
            ViewManagerModel viewManagerModel) {
        this.adoptPetViewModel = adoptPetViewModel;
        this.interactWithPetViewModel = interactWithPetViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(AdoptPetOutputData outputData) {
        InteractWithPetState state = new InteractWithPetState();

        state.setPetName(outputData.getPetName());
        state.setSpecies(outputData.getSpecies());
        state.setImageUrl(outputData.getImageUrl());

        interactWithPetViewModel.setState(state);
        interactWithPetViewModel.notifyStateChanged();

        viewManagerModel.setState(InteractWithPetViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        adoptPetViewModel.getState().setErrorMessage(errorMessage);
        adoptPetViewModel.notifyStateChanged();
    }
}