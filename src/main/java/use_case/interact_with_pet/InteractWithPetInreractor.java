package use_case.interact_with_pet;
import entity.Pet;

public class InteractWithPetInreractor implements InteractWithPetInputBoundary {

    private final PetDataAccessInterface petDataAccess;
    private InteractWithPetOutputBoundary presenter;

    public InteractWithPetInreractor (
            PetDataAccessInterface petDataAccess,
            InteractWithPetOutputBoundary presenter) {
        this.petDataAccess = petDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(InteractWithPetInputData inputData) {
        String username = inputData.getUsername();
        Pet pet = petDataAccess.findByOwnerUsername(username);

    if (pet == null){
        presenter.prepareAdoptionView(username);
    }
    else {
        InteractWithPetOutputData outputData =
                new InteractWithPetOutputData(
                        pet.getName(),
                        pet.getSpecies().name(),
                        pet.getImageUrl()
                );
        presenter.prepareInteractionView(outputData);
    }

    }
}
