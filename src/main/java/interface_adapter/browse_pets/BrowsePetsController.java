package interface_adapter.browse_pets;

import java.util.ArrayList;
import java.util.List;

import entity.Species;
import use_case.browse_pets.BrowsePetsInputBoundary;
import use_case.browse_pets.BrowsePetsInputData;
import use_case.browse_pets.PetFilter;
import use_case.browse_pets.SpeciesFilter;

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
     * @param species the species to show, or null for all
     */
    public void browse(Species species) {
        final List<PetFilter> filters = new ArrayList<>();
        if (species != null) {
            filters.add(new SpeciesFilter(species));
        }
        interactor.execute(new BrowsePetsInputData(filters));
    }
}
