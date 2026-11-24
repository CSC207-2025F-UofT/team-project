package interface_adapter.interact_with_pet;

import interface_adapter.ViewManagerModel;
import interface_adapter.adopt_pet.AdoptPetState;
import interface_adapter.adopt_pet.AdoptPetViewModel;
import use_case.interact_with_pet.InteractWithPetOutputBoundary;
import use_case.interact_with_pet.InteractWithPetOutputData;

public class InteractWithPetPresenter
        implements InteractWithPetOutputBoundary {

    private final AdoptPetViewModel adoptPetViewModel;
    private final InteractWithPetViewModel interactWithPetViewModel;
    private final ViewManagerModel viewManagerModel;

    public InteractWithPetPresenter(
            AdoptPetViewModel adoptPetViewModel,
            InteractWithPetViewModel interactWithPetViewModel,
            ViewManagerModel viewManagerModel) {
        this.adoptPetViewModel = adoptPetViewModel;
        this.interactWithPetViewModel = interactWithPetViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareAdoptionView(String username) {
        AdoptPetState state = new AdoptPetState();
        state.setUsername(username);

        adoptPetViewModel.setState(state);
        adoptPetViewModel.notifyStateChanged();

        viewManagerModel.setState(AdoptPetViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();
    }

    @Override
    public void prepareInteractionView(
            InteractWithPetOutputData outputData) {
        InteractWithPetState state = new InteractWithPetState();
        state.setPetName(outputData.getPetName());
        state.setSpecies(outputData.getSpecies());
        state.setImageUrl(outputData.getImageUrl());

        interactWithPetViewModel.setState(state);
        interactWithPetViewModel.notifyStateChanged();

        viewManagerModel.setState(InteractWithPetViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();
    }
}