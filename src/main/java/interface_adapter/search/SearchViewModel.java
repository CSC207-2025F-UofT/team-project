package interface_adapter.search;

import use_case.search.SearchOutputData;

import java.util.List;

public class SearchViewModel {
    private List<SearchOutputData.SongResult> results;
    private String errorMessage;

    public List<SearchOutputData.SongResult> getResults() {
        return results;
    }

    public void setResults(List<SearchOutputData.SongResult> results) {
        this.results = results;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void updateUI() {
// TODO: complete the update of UI
    }
}
