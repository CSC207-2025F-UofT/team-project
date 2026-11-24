package org.example.cocktailcraft.interface_adapter.search;

import org.example.cocktailcraft.use_case.search.SearchOutputBoundary;
import org.example.cocktailcraft.use_case.search.SearchOutputData;

public class SearchPresenter implements SearchOutputBoundary {

    private final SearchViewModel searchViewModel;

    public SearchPresenter(SearchViewModel searchViewModel) {
        this.searchViewModel = searchViewModel;
    }

    @Override
    public void prepareSuccessView(SearchOutputData searchOutputData) {
        final SearchState state = new SearchState();
        state.setResults(searchOutputData.getResults());
        state.setSearchPerformed(true);
        searchViewModel.setState(state);
        searchViewModel.notifyStateChanged();
    }

    @Override
    public void prepareFailureView(String errorMessage) {
        final SearchState state = new SearchState();
        state.setErrorMessage(errorMessage);
        state.setSearchPerformed(true);
        searchViewModel.setState(state);
        searchViewModel.notifyStateChanged();
    }
}
