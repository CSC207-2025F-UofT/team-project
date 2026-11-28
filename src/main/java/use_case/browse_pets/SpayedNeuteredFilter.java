package use_case.browse_pets;

import entity.Pet;

/**
 * Keeps only pets that are spayed or neutered.
 */
public class SpayedNeuteredFilter implements PetFilter {

    @Override
    public boolean matches(Pet pet) {
        return pet.isSpayedNeutered();
    }
}
