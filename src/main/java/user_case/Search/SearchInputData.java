package user_case.Search;

/**
 * The input data for the Search Use Case.
 */
public class SearchInputData {

    private final String title;


    public SearchInputData(String title) {
        this.title = title;
    }

    String getTitle() {
        return title;
    }

}
