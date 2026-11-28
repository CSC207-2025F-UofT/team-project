package use_case.browse_pets;

import entity.Pet;

/**
 * Keeps only pets that are vaccinated.
 */
public class VaccinatedFilter implements PetFilter {

    @Override
    public boolean matches(Pet pet) {
        return pet.isVaccinated();
    }
}
