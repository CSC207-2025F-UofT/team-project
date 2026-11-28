package use_case.browse_pets;

import java.util.List;
import java.util.stream.Collectors;

import entity.Pet;

/**
 * Lists available pets that pass every requested filter.
 */
public class BrowsePetsInteractor implements BrowsePetsInputBoundary {
    private final BrowsePetsDataAccessInterface dataAccess;
    private final BrowsePetsOutputBoundary presenter;

    /**
     * Creates the interactor.
     *
     * @param dataAccess where pets are read from
     * @param presenter where the result goes
     */
    public BrowsePetsInteractor(BrowsePetsDataAccessInterface dataAccess, BrowsePetsOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(BrowsePetsInputData inputData) {
        final List<PetFilter> filters = inputData.getFilters();
        final List<Pet> matches = dataAccess.getAvailablePets().stream()
                .filter(pet -> filters.stream().allMatch(filter -> filter.matches(pet)))
                .collect(Collectors.toList());
        presenter.presentPets(new BrowsePetsOutputData(matches));
    }
}
