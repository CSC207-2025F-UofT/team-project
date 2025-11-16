package interface_adapter.user_search;

import use_case.search_user.SearchUserInputBoundary;
import use_case.search_user.SearchUserInputData;

public class SearchUserController {
    private final SearchUserInputBoundary searchUserInteractor;
    public SearchUserController(SearchUserInputBoundary searchUserInteractor) {
        // 2. Initialize the field with the passed-in argument
        this.searchUserInteractor = searchUserInteractor; // <-- CRITICAL FIX
    }
    public void execute(String query) {
        SearchUserInputData inputData = new SearchUserInputData(query);
        searchUserInteractor.execute(inputData);
    }
}