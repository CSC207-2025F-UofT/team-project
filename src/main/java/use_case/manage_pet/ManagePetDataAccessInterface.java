package use_case.manage_pet;

import java.util.List;

import entity.Pet;

/** Pet storage operations required by the management use case. */
public interface ManagePetDataAccessInterface {

    /**
     * Loads the stored listings.
     * @return a snapshot of the pets
     */
    List<Pet> getPets();

    /**
     * Finds a pet by ID.
     * @param id the pet ID
     * @return the pet, or null when it is missing
     */
    Pet findById(String id);

    /**
     * Saves a new listing.
     * @param pet the new pet
     */
    void add(Pet pet);

    /**
     * Replaces an existing listing with the same ID.
     * @param pet the revised pet
     * @return whether the listing was updated
     */
    boolean update(Pet pet);

    /**
     * Deletes an existing listing.
     * @param id the pet ID
     * @return whether the listing was removed
     */
    boolean remove(String id);
}
