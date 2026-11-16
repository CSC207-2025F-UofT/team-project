package org.example.cocktailcraft.use_case.browse;

/** Scaffold only. TODO(Wenqian): inject the DAO and implement business rules. */
public class BrowseInteractor implements BrowseInputBoundary {
    private final BrowseOutputBoundary presenter;

    public BrowseInteractor(BrowseOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        // TODO(Wenqian): validate input, call the data access interface, and produce output.
        presenter.prepareFailView("Not implemented: Browse (Wenqian).");
    }
}
