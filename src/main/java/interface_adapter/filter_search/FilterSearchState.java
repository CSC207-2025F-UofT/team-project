package interface_adapter.filter_search;

import entity.Transaction;
import java.util.List;

public class FilterSearchState {

    private List<Transaction> results;
    private String errorMessage;

    public List<Transaction> getResults() {
        return results;
    }

    public void setResults(List<Transaction> results) {
        this.results = results;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String message) {
        this.errorMessage = message;
    }
}
