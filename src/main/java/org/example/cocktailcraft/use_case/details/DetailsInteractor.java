package org.example.cocktailcraft.use_case.details;

/** Scaffold only. TODO(Wenqian): inject the DAO and implement business rules. */
public class DetailsInteractor implements DetailsInputBoundary {
    private final DetailsOutputBoundary presenter;

    public DetailsInteractor(DetailsOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute(DetailsInputData inputData) {
        // TODO(Wenqian): validate input, call the data access interface, and produce output.
        presenter.prepareFailView("Not implemented: Details (Wenqian).");
    }
}
