package interface_adapter.ViewPets;

import interface_adapter.FilterPets.PetListViewModel;
import use_case.view_pets.ViewPetsOutputBoundary;
import use_case.view_pets.ViewPetsOutputData;

public class ViewPetsPresenter implements ViewPetsOutputBoundary {
    private final PetListViewModel viewModel;
    public ViewPetsPresenter(PetListViewModel viewModel) {
        this.viewModel = viewModel;
    }
    @Override
    public void present(ViewPetsOutputData outputData) {
        viewModel.setPets(outputData.getPets());
        viewModel.setErrorMessage(outputData.getErrorMsg());
    }
}
