package use_case.search;

import java.util.List;

/**
 * The Search Interactor.
 */
public class SearchInteractor implements SearchInputBoundary {
    private final SearchDataAccessInterface searchDataAccessObject;
    private final SearchOutputBoundary presenter;

    public SearchInteractor(SearchDataAccessInterface searchDataAccessInterface,
                            SearchOutputBoundary presenter) {
        this.searchDataAccessObject = searchDataAccessInterface;
        this.presenter = presenter;
    }

    @Override
    public void execute(SearchInputData inputData) {
        String results = searchDataAccessObject.search(inputData.getQuery());
        presenter.prepareSuccessView(results);
    }
}
