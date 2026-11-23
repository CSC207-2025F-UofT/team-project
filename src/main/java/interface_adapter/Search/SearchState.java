package interface_adapter.Search;

/**
 * The state for the Search View Model.
 */
public class SearchState {
    private String[][] outputMovieData;
    private String searchError;
    private String title = "";

    public String[][] getOutputMovieData() {
        return outputMovieData;
    }

    public String getSearchError() {
        return searchError;
    }


    public void setTtileError(String titleError) {
        this.searchError = titleError;
    }

    public String getTitle() { return title;}

    public void setTitle(String title) { this.title = title;}


    public void setOutputMovieData(String[][] outputMovieData) {
        this.outputMovieData = outputMovieData;
    }

}
