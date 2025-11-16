package use_case.search_user;

import java.util.List;

public class SearchUserInteractor implements SearchUserInputBoundary {
    private final SearchUserDataAccessInterface userDataAccessObject;
    private final SearchUserOutputBoundary userPresenter;

    public SearchUserInteractor(SearchUserDataAccessInterface userDataAccessObject,
                                 SearchUserOutputBoundary userPresenter) {
        this.userDataAccessObject = userDataAccessObject;
        this.userPresenter = userPresenter;
    }

    @Override
    public void execute(SearchUserInputData inputData) {
        String query = inputData.getQuery();

        if (query == null || query.trim().isEmpty()) {
            // If the query is empty, return all users or handle as a failure
            userPresenter.prepareFailView("Search query cannot be empty.");
            return;
        }

        List<String> results = userDataAccessObject.searchUsers(query);

        if (results.isEmpty()) {
            userPresenter.prepareFailView("No users found matching: " + query);
        } else {
            SearchUserOutputData outputData = new SearchUserOutputData(results);
            userPresenter.prepareSuccessView(outputData);
        }
    }
}