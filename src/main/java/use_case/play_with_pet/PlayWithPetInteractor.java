package use_case.play_with_pet;

import entity.Pet;

public class PlayWithPetInteractor
        implements PlayWithPetInputBoundary {

    private final PlayWithPetDataAccessInterface petDataAccess;
    private final PlayWithPetOutputBoundary presenter;

    public PlayWithPetInteractor(
            PlayWithPetDataAccessInterface petDataAccess,
            PlayWithPetOutputBoundary presenter) {
        this.petDataAccess = petDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(PlayWithPetInputData inputData) {
        String username = inputData.getUsername();

        if (username == null || username.isBlank()) {
            presenter.prepareFailView("Please log in first.");
            return;
        }

        Pet pet = petDataAccess.findByOwnerUsername(username);

        if (pet == null) {
            presenter.prepareFailView("Please adopt a pet first.");
            return;
        }

        pet.play(2);
        pet.play(2);

        try {
            petDataAccess.save(pet);
        }
        catch (IllegalStateException exception) {
            presenter.prepareFailView(
                    "Could not save your pet's progress. Please try again.");
            return;
        }

        PlayWithPetOutputData outputData =
                new PlayWithPetOutputData(pet.getHappiness());

        presenter.prepareSuccessView(outputData);
    }
}