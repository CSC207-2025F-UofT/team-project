package org.example.cocktailcraft.use_case.search;

/** Scaffold only. TODO(Qinli): inject the DAO and implement business rules. */
public class SearchInteractor implements SearchInputBoundary {
    private final SearchOutputBoundary presenter;

    public SearchInteractor(SearchOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute(SearchInputData inputData) {
        // TODO(Qinli): validate input, call the data access interface, and produce output.
        presenter.prepareFailView("Not implemented: Search (Qinli).");
    }
}
