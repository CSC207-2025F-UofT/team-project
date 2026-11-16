package org.example.cocktailcraft.interface_adapter.search;

import org.example.cocktailcraft.use_case.search.SearchOutputBoundary;
import org.example.cocktailcraft.use_case.search.SearchOutputData;

public class SearchPresenter implements SearchOutputBoundary {
    private final SearchViewModel viewModel;

    public SearchPresenter(SearchViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(SearchOutputData outputData) {
        // TODO(Qinli): map real output to state and request navigation if needed.
        throw new UnsupportedOperationException("Success mapping is not implemented: Search");
    }

    @Override
    public void prepareFailView(String message) {
        viewModel.getState().setMessage(message);
        viewModel.notifyStateChanged();
    }
}
