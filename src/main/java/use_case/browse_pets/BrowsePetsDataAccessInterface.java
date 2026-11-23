package use_case.browse_pets;

import java.util.List;

import entity.Pet;

/**
 * Where the gallery reads pets from.
 */
public interface BrowsePetsDataAccessInterface {

    /**
     * Returns every pet that can be adopted right now.
     *
     * @return the available pets, empty if none or if the source failed
     */
    List<Pet> getAvailablePets();
}
