package interface_adapter.pet_management;

import use_case.pet_management.*;

public class PetManagementPresenter implements PetManagementOutputBoundary {

    private final PetManagementViewModel viewModel;

    public PetManagementPresenter(PetManagementViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentAdd(AddPetOutputData outputData) {
        viewModel.setStatusMessage(outputData.getMessage());
    }

    @Override
    public void presentUpdate(UpdatePetOutputData outputData) {
        viewModel.setStatusMessage(outputData.getMessage());
    }

    @Override
    public void presentDelete(DeletePetOutputData outputData) {
        viewModel.setStatusMessage(outputData.getMessage());
    }
}
