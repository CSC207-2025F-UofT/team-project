package interface_adapter.Search;

import user_case.Search.SearchInputBoundary;
import user_case.Search.SearchInputData;

/**
 * The controller for the Search Use Case.
 */
public class SearchController {

    private final SearchInputBoundary searchInteractor;

    public SearchController(SearchInputBoundary searchInteractor) {
        this.searchInteractor = searchInteractor;
    }

    /**
     * Executes the Search Use Case.
     * @param title the title of the movie to be searched
     */
    public void execute(String title) {
        final SearchInputData searchInputData = new SearchInputData(title);

        searchInteractor.execute(searchInputData);
    }
}
