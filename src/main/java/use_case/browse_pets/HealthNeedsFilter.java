package use_case.browse_pets;

import entity.Pet;

/**
 * Keeps pets by whether they need daily medication. Use {@code false} to show only pets with no medical needs.
 */
public class HealthNeedsFilter implements PetFilter {
    private final boolean needsMedication;

    /**
     * Creates a filter on medication needs.
     *
     * @param needsMedication true to keep pets that need medication, false to keep pets that do not
     */
    public HealthNeedsFilter(boolean needsMedication) {
        this.needsMedication = needsMedication;
    }

    @Override
    public boolean matches(Pet pet) {
        return pet.needsMedication() == needsMedication;
    }
}
