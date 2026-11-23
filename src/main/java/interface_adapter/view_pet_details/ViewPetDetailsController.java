package interface_adapter.view_pet_details;

import use_case.view_pet_details.ViewPetDetailsInputBoundary;
import use_case.view_pet_details.ViewPetDetailsInputData;

/**
 * Receives a click on a cat from the gallery and starts the view pet details use case.
 */
public class ViewPetDetailsController {
    private final ViewPetDetailsInputBoundary interactor;

    /**
     * Creates the controller.
     *
     * @param interactor the use case to run
     */
    public ViewPetDetailsController(ViewPetDetailsInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Opens the detail page for one cat.
     *
     * @param petId the id of the cat to show
     */
    public void show(String petId) {
        interactor.execute(new ViewPetDetailsInputData(petId));
    }
}
