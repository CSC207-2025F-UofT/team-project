package use_case.browse_pets;

import java.util.Set;

import entity.Pet;
import entity.Temperament;

/**
 * Keeps only pets that have every temperament tag the visitor picked. Picking none keeps every pet.
 */
public class TemperamentFilter implements PetFilter {
    private final Set<Temperament> required;

    /**
     * Creates a filter for temperament tags.
     *
     * @param required the tags a pet must all have, may be empty
     */
    public TemperamentFilter(Set<Temperament> required) {
        this.required = Set.copyOf(required);
    }

    @Override
    public boolean matches(Pet pet) {
        return pet.getTemperamentTags().containsAll(required);
    }
}
