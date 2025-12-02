package use_case.pet_management;

import entity.Pet;

public class AddPetInteractor implements AddPetInputBoundary {

    private final PetDataAccessInterface petGateway;
    private final PetManagementOutputBoundary presenter;

    public AddPetInteractor(PetDataAccessInterface petGateway,
                            PetManagementOutputBoundary presenter) {
        this.petGateway = petGateway;
        this.presenter = presenter;
    }

    @Override
    public void execute(AddPetInputData inputData) {
        if (petGateway.existsById(inputData.id)) {
            presenter.presentAdd(new AddPetOutputData(false, "Pet ID already exists."));
            return;
        }

        try {
            // Map strings into your existing enums
            Pet.Age age = Pet.Age.valueOf(inputData.age.toUpperCase());
            Pet.Gender gender = Pet.Gender.valueOf(inputData.gender.toUpperCase());
            Pet.Size size = Pet.Size.valueOf(inputData.size.toUpperCase());

            Pet pet = new Pet(
                    inputData.id,
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

            petGateway.save(pet);
            presenter.presentAdd(new AddPetOutputData(true, "Pet added successfully."));
        } catch (IllegalArgumentException e) {
            presenter.presentAdd(new AddPetOutputData(false, "Invalid pet data: " + e.getMessage()));
        }
    }
}
