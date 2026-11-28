package use_case.view_pet_details;

import java.util.Optional;

import entity.Pet;

/**
 * Where the detail page looks a cat up.
 */
public interface ViewPetDetailsDataAccessInterface {

    /**
     * Finds one cat by its id.
     *
     * @param petId the id of the cat
     * @return the cat, or empty if there is none with that id or the source failed
     */
    Optional<Pet> findById(String petId);
}
