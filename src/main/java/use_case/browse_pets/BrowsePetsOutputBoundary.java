package use_case.browse_pets;

/**
 * Where the use case sends its result, implemented by the presenter.
 */
public interface BrowsePetsOutputBoundary {

    /**
     * Shows the pets that matched.
     *
     * @param outputData the matching pets
     */
    void presentPets(BrowsePetsOutputData outputData);
}
