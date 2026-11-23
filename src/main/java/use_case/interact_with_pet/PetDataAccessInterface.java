package use_case.interact_with_pet;

import entity.Pet;

public interface PetDataAccessInterface {

    /**
     * Returns the user's pet, or null if they have not adopted one.
     */
    Pet findByOwnerUsername(String username);
}