package interface_adapter.pet_management;

import use_case.pet_management.*;

public class PetManagementController {

    private final AddPetInputBoundary addPetUseCase;
    private final UpdatePetInputBoundary updatePetUseCase;
    private final DeletePetInputBoundary deletePetUseCase;

    public PetManagementController(AddPetInputBoundary addPetUseCase,
                                   UpdatePetInputBoundary updatePetUseCase,
                                   DeletePetInputBoundary deletePetUseCase) {
        this.addPetUseCase = addPetUseCase;
        this.updatePetUseCase = updatePetUseCase;
        this.deletePetUseCase = deletePetUseCase;
    }

    public void addPet(String idText,
                       String name,
                       String age,
                       String gender,
                       String species,
                       String breed,
                       String colour,
                       String size,
                       String description,
                       String imageURL) {

        try {
            int id = Integer.parseInt(idText.trim());

            AddPetInputData inputData = new AddPetInputData(
                    id, name, age, gender, species, breed, colour, size, description, imageURL
            );
            addPetUseCase.execute(inputData);
        } catch (NumberFormatException e) {
            // You can hook this up to an error state later.
        }
    }

    public void updatePet(String idText,
                          String name,
                          String age,
                          String gender,
                          String species,
                          String breed,
                          String colour,
                          String size,
                          String description,
                          String imageURL) {

        try {
            int id = Integer.parseInt(idText.trim());

            UpdatePetInputData inputData = new UpdatePetInputData(
                    id, name, age, gender, species, breed, colour, size, description, imageURL
            );
            updatePetUseCase.execute(inputData);
        } catch (NumberFormatException e) {
            // handle invalid ID later
        }
    }

    public void deletePet(String idText) {
        try {
            int id = Integer.parseInt(idText.trim());
            DeletePetInputData inputData = new DeletePetInputData(id);
            deletePetUseCase.execute(inputData);
        } catch (NumberFormatException e) {
            // handle invalid ID later
        }
    }
}
