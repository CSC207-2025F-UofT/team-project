package use_case.view_pet_details;

import java.util.Optional;

import entity.Pet;
import entity.PetStatus;

/**
 * Looks up one available cat for the detail page.
 */
public class ViewPetDetailsInteractor implements ViewPetDetailsInputBoundary {
    private final ViewPetDetailsDataAccessInterface dataAccess;
    private final ViewPetDetailsOutputBoundary presenter;

    /**
     * Creates the interactor.
     *
     * @param dataAccess where cats are looked up
     * @param presenter where the result goes
     */
    public ViewPetDetailsInteractor(ViewPetDetailsDataAccessInterface dataAccess,
                                    ViewPetDetailsOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(ViewPetDetailsInputData inputData) {
        final Optional<Pet> found = dataAccess.findById(inputData.getPetId());
        if (found.isPresent() && found.get().getStatus() == PetStatus.AVAILABLE) {
            presenter.presentPet(new ViewPetDetailsOutputData(found.get()));
        } else {
            presenter.presentNotFound("This cat is no longer available.");
        }
    }
}
