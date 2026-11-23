package use_case.browse_pets;

import entity.Pet;

/**
 * One rule for deciding whether a pet shows up in the gallery. New filters are new classes.
 */
public interface PetFilter {

    /**
     * Checks a pet against this filter.
     *
     * @param pet the pet to check
     * @return true if the pet should stay in the gallery
     */
    boolean matches(Pet pet);
}
