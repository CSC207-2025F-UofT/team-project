package interface_adapter.search;

import use_case.search.SearchInputBoundary;
import use_case.search.SearchInputData;

/**
 * The controller for the Login Use Case.
 */
public class SearchController {

    private final SearchInputBoundary searchUseCaseInteractor;

    public SearchController(SearchInputBoundary searchUseCaseInteractor) {
        this.searchUseCaseInteractor = searchUseCaseInteractor;
    }

    /**
     * Executes the Login Use Case.
     * @param locationName the locationName of the location search
     */
    public void execute(String locationName) throws Exception {
        final SearchInputData searchInputData = new SearchInputData(
                locationName);

        searchUseCaseInteractor.execute(searchInputData);
    }
}
