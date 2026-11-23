package interface_adapter.browse_pets;

import java.util.List;

import use_case.browse_pets.BrowsePetsInputBoundary;
import use_case.browse_pets.BrowsePetsInputData;
import use_case.browse_pets.PetFilter;

/**
 * Receives the visitor's choices from the gallery view and starts the browse use case.
 */
public class BrowsePetsController {
    private final BrowsePetsInputBoundary interactor;

    /**
     * Creates the controller.
     *
     * @param interactor the use case to run
     */
    public BrowsePetsController(BrowsePetsInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Loads the gallery with the chosen filters.
     *
     * @param filters the filters to apply, empty to show every available pet
     */
    public void browse(List<PetFilter> filters) {
        interactor.execute(new BrowsePetsInputData(filters));
    }
}
