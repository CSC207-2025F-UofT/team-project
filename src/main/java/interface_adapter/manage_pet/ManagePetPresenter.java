package interface_adapter.manage_pet;

import java.util.ArrayList;
import java.util.List;

import use_case.manage_pet.ManagePetOutputBoundary;
import use_case.manage_pet.ManagePetOutputData;
import use_case.manage_pet.ManagePetOutputData.PetDetails;

/** Adapts use case output to the observed pet management view model. */
public final class ManagePetPresenter implements ManagePetOutputBoundary {
    private final ManagePetViewModel viewModel;

    /**
     * Creates a presenter for a management screen.
     * @param viewModel the display state to update
     */
    public ManagePetPresenter(ManagePetViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(ManagePetOutputData outputData) {
        final List<PetViewData> listings = new ArrayList<>();
        final List<PetViewData> available = new ArrayList<>();
        for (PetDetails details : outputData.getPets()) {
            final PetViewData listing = new PetViewData(details);
            listings.add(listing);
            if (details.isAvailable()) {
                available.add(listing);
            }
        }
        viewModel.showListings(listings, available, outputData.getMessage());
    }

    @Override
    public void prepareFailView(String message) {
        viewModel.showError(message);
    }
}
