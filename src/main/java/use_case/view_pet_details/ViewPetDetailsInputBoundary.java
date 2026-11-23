package use_case.view_pet_details;

/**
 * Entry point of the view pet details use case, called by the controller.
 */
public interface ViewPetDetailsInputBoundary {

    /**
     * Looks up one cat and sends it to the presenter.
     *
     * @param inputData which cat to show
     */
    void execute(ViewPetDetailsInputData inputData);
}
