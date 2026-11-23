package use_case.interact_with_pet;

public interface InteractWithPetOutputBoundary {

    void prepareAdoptionView(String username);

    void prepareInteractionView(InteractWithPetOutputData outputData);
}
