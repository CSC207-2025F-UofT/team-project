package use_case.adopt_pet;

import entity.Pet;

public class AdoptPetInteractor implements AdoptPetInputBoundary {

    private final AdoptPetDataAccessInterface petDataAccess;
    private final AdoptPetOutputBoundary presenter;

    public AdoptPetInteractor(
            AdoptPetDataAccessInterface petDataAccess,
            AdoptPetOutputBoundary presenter) {
        this.petDataAccess = petDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(AdoptPetInputData inputData) {
        String username = inputData.getUsername();
        String species = inputData.getSpecies();
        String petName = inputData.getPetName();
        String imageUrl = inputData.getImageUrl();

        if (username == null || username.isBlank()) {
            presenter.prepareFailView("Please log in before adopting.");
            return;
        }

        if (petDataAccess.findByOwnerUsername(username) != null) {
            presenter.prepareFailView("You already have a pet.");
            return;
        }

        if (!"DOG".equals(species) && !"CAT".equals(species)) {
            presenter.prepareFailView("Please choose DOG or CAT.");
            return;
        }

        if (petName == null || petName.isBlank()) {
            presenter.prepareFailView("Please enter a pet name.");
            return;
        }

        if (imageUrl == null || imageUrl.isBlank()) {
            presenter.prepareFailView("Please choose a picture.");
            return;
        }

        String trimmedName = petName.trim();

        Pet pet = new Pet(
                username,
                trimmedName,
                Pet.Species.valueOf(species),
                imageUrl
        );

        petDataAccess.save(pet);

        AdoptPetOutputData outputData = new AdoptPetOutputData(
                pet.getName(),
                pet.getSpecies().name(),
                pet.getImageUrl()
        );

        presenter.prepareSuccessView(outputData);
    }
}