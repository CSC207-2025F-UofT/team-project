package interface_adapter.Search;

import interface_adapter.ViewManagerModel;
import user_case.Search.SearchOutputBoundary;
import user_case.Search.SearchOutputData;

/**
 * The Presenter for the Search Use Case.
 */
public class SearchPresenter implements SearchOutputBoundary {

    private final SearchBarViewModel searchBarViewModel;
    private final SearchedViewModel searchedViewModel;
    private final ViewManagerModel viewManagerModel;

    public SearchPresenter(ViewManagerModel viewManagerModel,
                          SearchBarViewModel searchBarViewModel,
                          SearchedViewModel searchedViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.searchBarViewModel = searchBarViewModel;
        this.searchedViewModel = searchedViewModel;
    }

    @Override
    public void prepareSuccessView(SearchOutputData response) {
        // On success, update the loggedInViewModel's state
        final SearchState searchedState = searchedViewModel.getState();
        searchedState.setOutputMovieData(response.getOutputList());
        this.searchedViewModel.notifyStateChanged();

        // and clear everything from the LoginViewModel's state
        this.searchBarViewModel.setState(new SearchState());
        this.searchBarViewModel.notifyStateChanged();

        // switch to the logged in view
        this.viewManagerModel.setState(searchedViewModel.VIEW_NAME);
        this.viewManagerModel.notifyViewChanged();
    }

    @Override
    public void prepareFailView(String error) {
        final SearchState loginState = searchBarViewModel.getState();
        loginState.setTtileError(error);
        searchBarViewModel.notifyStateChanged();
    }
}
