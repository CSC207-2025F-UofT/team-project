package use_case.view_pet_details;

/**
 * Where the use case sends its result, implemented by the presenter.
 */
public interface ViewPetDetailsOutputBoundary {

    /**
     * Shows the detail page for a cat.
     *
     * @param outputData the cat to show
     */
    void presentPet(ViewPetDetailsOutputData outputData);

    /**
     * Tells the visitor the cat could not be shown.
     *
     * @param message what went wrong, in plain words
     */
    void presentNotFound(String message);
}
