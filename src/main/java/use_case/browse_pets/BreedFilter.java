package use_case.browse_pets;

import java.util.Locale;

import entity.Pet;

/**
 * Keeps pets whose breed contains the text the visitor typed, ignoring upper and lower case.
 * Typing nothing keeps every pet.
 */
public class BreedFilter implements PetFilter {
    private final String text;

    /**
     * Creates a filter on breed.
     *
     * @param text part of a breed name, for example "tab" for Tabby; blank keeps every pet
     */
    public BreedFilter(String text) {
        this.text = text == null ? "" : text.strip().toLowerCase(Locale.ROOT);
    }

    @Override
    public boolean matches(Pet pet) {
        return pet.getBreed().toLowerCase(Locale.ROOT).contains(text);
    }
}
