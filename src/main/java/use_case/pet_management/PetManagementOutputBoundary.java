package use_case.pet_management;

public interface PetManagementOutputBoundary {

    void presentAdd(AddPetOutputData outputData);

    void presentUpdate(UpdatePetOutputData outputData);

    void presentDelete(DeletePetOutputData outputData);
}
