package use_case.manage_pet;

import java.util.Optional;

/** Validates a breed lookup independently of saving a pet listing. */
public final class BreedLookupInteractor implements BreedLookupInputBoundary {
    private static final int MAX_BREED_NAME_LENGTH = 30;

    private final BreedReferenceDataAccessInterface dataAccess;

    /**
     * Creates the lookup use case.
     * @param dataAccess the breed reference provider
     */
    public BreedLookupInteractor(BreedReferenceDataAccessInterface dataAccess) {
        this.dataAccess = dataAccess;
    }

    @Override
    public Optional<BreedReference> lookup(String breedName) {
        final String query = breedName.strip();
        if (query.isEmpty() || query.length() > MAX_BREED_NAME_LENGTH) {
            throw new IllegalArgumentException("Enter a breed name of 1 to 30 characters to look it up.");
        }
        return dataAccess.findByName(query);
    }
}
