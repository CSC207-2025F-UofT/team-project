package use_case.browse_pets;

import entity.Pet;
import entity.PetSex;

/**
 * Keeps pets of one sex.
 */
public class SexFilter implements PetFilter {
    private final PetSex sex;

    /**
     * Creates a filter on sex.
     *
     * @param sex the sex to keep
     */
    public SexFilter(PetSex sex) {
        if (sex == null) {
            throw new IllegalArgumentException("Sex is required.");
        }
        this.sex = sex;
    }

    @Override
    public boolean matches(Pet pet) {
        return pet.getSex() == sex;
    }
}
