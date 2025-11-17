package use_case.search_user;

public class SearchUserInputData {
    private final String query;

    public SearchUserInputData(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}