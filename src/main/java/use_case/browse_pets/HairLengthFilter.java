package use_case.browse_pets;

import entity.Pet;

/** Keeps pets with the selected hair length. */
public final class HairLengthFilter implements PetFilter {
    private final String length;

    /** Creates a filter for the selected coat length. */
    public HairLengthFilter(String length) {
        this.length = length.strip();
    }

    @Override
    public boolean matches(Pet pet) {
        return pet.getProfileDetails().getHairLength().equalsIgnoreCase(length);
    }
}
