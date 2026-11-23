package interface_adapter.browse_pets;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import entity.Temperament;
import use_case.browse_pets.AgeRangeFilter;
import use_case.browse_pets.BrowsePetsInputBoundary;
import use_case.browse_pets.BrowsePetsInputData;
import use_case.browse_pets.HealthNeedsFilter;
import use_case.browse_pets.PetFilter;
import use_case.browse_pets.TemperamentFilter;

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
     * Loads the gallery using what the visitor picked in the filter panel.
     *
     * @param age the age choice, {@link AgeFilterOption#ANY} for no age filter
     * @param temperaments the tags a cat must all have, empty for none
     * @param noMedicalNeedsOnly true to show only cats that need no medication
     */
    public void browse(AgeFilterOption age, Set<Temperament> temperaments, boolean noMedicalNeedsOnly) {
        final List<PetFilter> filters = new ArrayList<>();
        if (age.getMinYears() != null) {
            filters.add(new AgeRangeFilter(age.getMinYears(), age.getMaxYears()));
        }
        if (!temperaments.isEmpty()) {
            filters.add(new TemperamentFilter(temperaments));
        }
        if (noMedicalNeedsOnly) {
            filters.add(new HealthNeedsFilter(false));
        }
        interactor.execute(new BrowsePetsInputData(filters));
    }
}
