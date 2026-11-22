package org.example.cocktailcraft.interface_adapter.search;

import org.example.cocktailcraft.use_case.search.SearchOutputBoundary;
import org.example.cocktailcraft.use_case.search.SearchOutputData;

import java.util.List;

public class SearchPresenter implements SearchOutputBoundary {

    private final SearchViewModel searchViewModel;

    public SearchPresenter(SearchViewModel searchViewModel) {
        this.searchViewModel = searchViewModel;
    }

    @Override
    public void prepareSuccessView(SearchOutputData searchOutputData) {
        searchViewModel.setResults(searchOutputData.getResults());
        searchViewModel.setErrorMessage("");
        searchViewModel.notifyStateChanged();
    }

    public void prepareFailureView(String errorMessage) {
        searchViewModel.setResults(List.of());
        searchViewModel.setErrorMessage(errorMessage);
        searchViewModel.notifyStateChanged();
    }
}
