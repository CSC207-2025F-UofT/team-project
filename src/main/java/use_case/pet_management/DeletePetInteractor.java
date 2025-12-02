package use_case.pet_management;

public class DeletePetInteractor implements DeletePetInputBoundary {

    private final PetDataAccessInterface petGateway;
    private final PetManagementOutputBoundary presenter;

    public DeletePetInteractor(PetDataAccessInterface petGateway,
                               PetManagementOutputBoundary presenter) {
        this.petGateway = petGateway;
        this.presenter = presenter;
    }

    @Override
    public void execute(DeletePetInputData inputData) {
        if (!petGateway.existsById(inputData.id)) {
            presenter.presentDelete(new DeletePetOutputData(false, "Pet ID not found."));
            return;
        }

        petGateway.deleteById(inputData.id);
        presenter.presentDelete(new DeletePetOutputData(true, "Pet deleted successfully."));
    }
}
