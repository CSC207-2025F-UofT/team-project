package interface_adapter.presenters4ViewFilterPets;

import interface_adapter.ViewModel4ViewFilterPets.PetListViewModel;
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
