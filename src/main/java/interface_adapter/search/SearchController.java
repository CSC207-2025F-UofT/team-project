package interface_adapter.search;

import use_case.search.SearchInputBoundary;
import use_case.search.SearchInputData;

/**
 * Controller for the Search Use Case.
 */
public class SearchController {
    private final SearchInputBoundary userSearchUseCaseInteractor;

    public SearchController(SearchInputBoundary userSearchUseCaseInteractor) {
        this.userSearchUseCaseInteractor = userSearchUseCaseInteractor;
    }

    /**
     * Executes the Search Use Case.
     * @param query the query typed in
     */
    public void execute(String query) {
        final SearchInputData signupInputData = new SearchInputData(query);

        userSearchUseCaseInteractor.execute(signupInputData);
    }
}
