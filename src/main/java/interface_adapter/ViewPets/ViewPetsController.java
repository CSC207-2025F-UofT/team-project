package interface_adapter.ViewPets;

import interface_adapter.FilterPets.PetListViewModel;
import use_case.view_pets.ViewPetsInputBoundary;
import use_case.view_pets.ViewPetsInputData;

public class ViewPetsController {
    final ViewPetsInputBoundary interactor;
    private final PetListViewModel viewModel;

    public ViewPetsController(ViewPetsInputBoundary interactor,
            PetListViewModel viewModel) {
        this.interactor = interactor;
        this.viewModel = viewModel;
    }
    
    public void onViewPets() {
        interactor.execute(new ViewPetsInputData());
    }
    
    public PetListViewModel getViewModel() {
        return viewModel;
    }
}
