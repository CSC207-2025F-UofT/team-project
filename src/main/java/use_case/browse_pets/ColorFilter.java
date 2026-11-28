package use_case.browse_pets;

import java.util.Locale;

import entity.Pet;

/** Keeps pets whose listed coat color contains the search text. */
public final class ColorFilter implements PetFilter {
    private final String text;

    /** Creates a case-insensitive color search. */
    public ColorFilter(String text) {
        this.text = text.strip().toLowerCase(Locale.ROOT);
    }

    @Override
    public boolean matches(Pet pet) {
        return pet.getProfileDetails().getColor().toLowerCase(Locale.ROOT).contains(text);
    }
}
