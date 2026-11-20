package use_case.browse_pets;

import entity.Pet;
import entity.Species;

/**
 * Keeps only pets of one species.
 */
public class SpeciesFilter implements PetFilter {
    private final Species species;

    /**
     * Creates a filter for one species.
     *
     * @param species the species to keep
     */
    public SpeciesFilter(Species species) {
        this.species = species;
    }

    @Override
    public boolean matches(Pet pet) {
        return pet.getSpecies() == species;
    }
}
