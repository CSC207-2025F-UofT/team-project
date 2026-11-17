package interface_adaptor.search;

import interface_adaptor.ViewManagerModel;
import use_case.search.SearchOutputData;

public class SearchPresenter {
    private final SearchViewModel searchViewModel;
    private final ViewManagerModel viewManagerModel;

    public SearchPresenter(ViewManagerModel viewManagerModel,
                           SearchViewModel searchViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.searchViewModel = searchViewModel;
    }

    public void prepareSuccessView(SearchOutputData response) {
        final SearchState searchState = searchViewModel.getState();
        searchState.setResult(response.getResult());
        searchViewModel.notifyStateChanged();

        viewManagerModel.setState(SearchViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();
    }

    public void prepareFailView(String error) {
        final SearchState searchState = searchViewModel.getState();
        searchState.setResultError(error);
        searchViewModel.notifyStateChanged();
    }
}
