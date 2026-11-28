package interface_adapter.check_compatibility;

import use_case.check_compatibility.CheckCompatibilityInputBoundary;
import use_case.check_compatibility.CheckCompatibilityInputData;

/**
 * Starts the compatibility check when the detail page asks for it.
 */
public class CheckCompatibilityController {
    private final CheckCompatibilityInputBoundary interactor;

    /**
     * Creates the controller.
     *
     * @param interactor the use case to run
     */
    public CheckCompatibilityController(CheckCompatibilityInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Checks one cat against the adopter's saved preferences.
     *
     * @param petId the cat being viewed
     * @param adopterId the adopter whose preferences are used
     */
    public void check(String petId, String adopterId) {
        interactor.execute(new CheckCompatibilityInputData(petId, adopterId));
    }
}
