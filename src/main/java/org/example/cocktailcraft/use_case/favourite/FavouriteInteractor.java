package org.example.cocktailcraft.use_case.favourite;

/** Scaffold only. TODO(Wenqian): inject the DAO and implement business rules. */
public class FavouriteInteractor implements FavouriteInputBoundary {
    private final FavouriteOutputBoundary presenter;

    public FavouriteInteractor(FavouriteOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute(FavouriteInputData inputData) {
        // TODO(Wenqian): validate input, call the data access interface, and produce output.
        presenter.prepareFailView("Not implemented: Favourite (Wenqian).");
    }
}
