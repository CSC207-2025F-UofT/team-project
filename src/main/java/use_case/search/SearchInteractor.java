package use_case.search;

import entity.UserFactory;

import java.util.List;

public class SearchInteractor implements SearchInputBoundary {
    private final SearchDataAccessInterface searchDataAccessObject;
    private final SearchOutputBoundary presenter;

    public SearchInteractor(SearchDataAccessInterface searchDataAccessInterface,
                            SearchOutputBoundary presenter) {
        this.searchDataAccessObject = searchDataAccessInterface;
        this.presenter = presenter;
    }

    public void execute(SearchInputData inputData) {
        List<String> results = searchDataAccessObject.search(inputData.getQuery());
        presenter.prepareSuccessView(results);
    }
}
