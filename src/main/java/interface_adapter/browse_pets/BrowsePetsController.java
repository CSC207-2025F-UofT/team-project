package interface_adapter.browse_pets;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import entity.Temperament;
import use_case.browse_pets.AgeRangeFilter;
import use_case.browse_pets.BreedFilter;
import use_case.browse_pets.BrowsePetsInputBoundary;
import use_case.browse_pets.BrowsePetsInputData;
import use_case.browse_pets.HealthNeedsFilter;
import use_case.browse_pets.PetFilter;
import use_case.browse_pets.SexFilter;
import use_case.browse_pets.SpayedNeuteredFilter;
import use_case.browse_pets.TemperamentFilter;
import use_case.browse_pets.VaccinatedFilter;

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
     * Loads the gallery using the age, temperament and medical needs the visitor picked.
     *
     * @param age the age choice, {@link AgeFilterOption#ANY} for no age filter
     * @param temperaments the tags a cat must all have, empty for none
     * @param noMedicalNeedsOnly true to show only cats that need no medication
     */
    public void browse(AgeFilterOption age, Set<Temperament> temperaments, boolean noMedicalNeedsOnly) {
        final BrowseChoices choices = new BrowseChoices();
        choices.setAge(age);
        choices.setTemperaments(temperaments);
        choices.setNoMedicalNeedsOnly(noMedicalNeedsOnly);
        browse(choices);
    }

    /**
     * Loads the gallery using everything the visitor picked in the filter panel.
     *
     * @param choices the visitor's choices
     */
    public void browse(BrowseChoices choices) {
        final List<PetFilter> filters = new ArrayList<>();
        final AgeFilterOption age = choices.getAge();
        if (age.getMinYears() != null) {
            filters.add(new AgeRangeFilter(age.getMinYears(), age.getMaxYears()));
        }
        if (choices.getSex().getSex() != null) {
            filters.add(new SexFilter(choices.getSex().getSex()));
        }
        if (!choices.getBreed().isBlank()) {
            filters.add(new BreedFilter(choices.getBreed()));
        }
        if (!choices.getTemperaments().isEmpty()) {
            filters.add(new TemperamentFilter(choices.getTemperaments()));
        }
        if (choices.isNoMedicalNeedsOnly()) {
            filters.add(new HealthNeedsFilter(false));
        }
        if (choices.isVaccinatedOnly()) {
            filters.add(new VaccinatedFilter());
        }
        if (choices.isSpayedNeuteredOnly()) {
            filters.add(new SpayedNeuteredFilter());
        }
        interactor.execute(new BrowsePetsInputData(filters));
    }
}
