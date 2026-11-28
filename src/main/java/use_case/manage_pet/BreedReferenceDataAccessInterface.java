package use_case.manage_pet;

import java.util.Optional;

/** Reads general cat breed information from a provider. */
public interface BreedReferenceDataAccessInterface {
    /**
     * Finds a reference for the requested breed.
     * @param name the breed name
     * @return the reference, or empty when the breed is unknown
     * @throws IllegalStateException when the provider is unavailable
     */
    Optional<BreedReference> findByName(String name);
}
