package use_case.browse_pets;

import java.util.List;

import entity.Pet;

/**
 * The pets that matched, handed to the presenter.
 */
public class BrowsePetsOutputData {
    private final List<Pet> pets;

    /**
     * Creates the result.
     *
     * @param pets the matching pets
     */
    public BrowsePetsOutputData(List<Pet> pets) {
        this.pets = List.copyOf(pets);
    }

    /**
     * Returns the matching pets.
     *
     * @return the pets
     */
    public List<Pet> getPets() {
        return pets;
    }
}
