package app;

import interface_adapter.manage_pet.ManagePetController;
import interface_adapter.manage_pet.ManagePetPresenter;
import interface_adapter.manage_pet.ManagePetViewModel;
import use_case.manage_pet.ManagePetDataAccessInterface;
import use_case.manage_pet.ManagePetInteractor;
import view.ManagePetView;

/** Wires the application screens and their use cases. */
public final class AppBuilder {

    /**
     * Builds the pet management screen for the admin flow.
     * @param petDataAccess storage shared with the other pet features
     * @return the wired management view
     */
    public ManagePetView buildPetManagement(ManagePetDataAccessInterface petDataAccess) {
        final ManagePetViewModel viewModel = new ManagePetViewModel();
        final ManagePetPresenter presenter = new ManagePetPresenter(viewModel);
        final ManagePetInteractor interactor = new ManagePetInteractor(petDataAccess, presenter);
        final ManagePetController controller = new ManagePetController(interactor);
        final ManagePetView view = new ManagePetView(controller, viewModel);
        controller.refresh();
        return view;
    }
}
