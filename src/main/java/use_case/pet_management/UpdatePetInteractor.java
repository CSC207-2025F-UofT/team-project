package use_case.pet_management;

import entity.Pet;

public class UpdatePetInteractor implements UpdatePetInputBoundary {

    private final PetDataAccessInterface petGateway;
    private final PetManagementOutputBoundary presenter;

    public UpdatePetInteractor(PetDataAccessInterface petGateway,
                               PetManagementOutputBoundary presenter) {
        this.petGateway = petGateway;
        this.presenter = presenter;
    }

    @Override
    public void execute(UpdatePetInputData inputData) {
        if (!petGateway.existsById(inputData.id)) {
            presenter.presentUpdate(new UpdatePetOutputData(false, "Pet ID not found."));
            return;
        }

        try {
            Pet existing = petGateway.getById(inputData.id);

            Pet.Age age = Pet.Age.valueOf(inputData.age.toUpperCase());
            Pet.Gender gender = Pet.Gender.valueOf(inputData.gender.toUpperCase());
            Pet.Size size = Pet.Size.valueOf(inputData.size.toUpperCase());

            Pet updated = new Pet(
                    existing.getId(),
                    inputData.name,
                    age,
                    gender,
                    inputData.species,
                    inputData.breed,
                    inputData.colour,
                    size,
                    inputData.description,
                    inputData.imageURL
            );

            petGateway.save(updated);
            presenter.presentUpdate(new UpdatePetOutputData(true, "Pet updated successfully."));
        } catch (IllegalArgumentException e) {
            presenter.presentUpdate(new UpdatePetOutputData(false, "Invalid pet data: " + e.getMessage()));
        }
    }
}
