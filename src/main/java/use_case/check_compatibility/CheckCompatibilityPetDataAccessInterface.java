package use_case.check_compatibility;

import java.util.Optional;

import entity.Pet;

/**
 * Where the compatibility check finds the cat.
 */
public interface CheckCompatibilityPetDataAccessInterface {

    /**
     * Finds a cat by its id.
     *
     * @param petId the pet id
     * @return the cat, or empty if there is none
     */
    Optional<Pet> findById(String petId);
}
