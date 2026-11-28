package use_case.manage_pet;

import java.util.Optional;

/** Looks up optional reference information while the admin edits a listing. */
public interface BreedLookupInputBoundary {
    /**
     * Finds typical traits and a reference photo.
     * @param breedName the breed typed on the pet form
     * @return the matching reference, if found
     */
    Optional<BreedReference> lookup(String breedName);
}
