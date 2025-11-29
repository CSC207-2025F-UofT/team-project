package use_case.filter_search;

import entity.Transaction;
import java.util.List;

public class FilterSearchOutputData {

    private final List<Transaction> results;

    public FilterSearchOutputData(List<Transaction> results) {
        this.results = results;
    }

    public List<Transaction> getResults() {
        return results;
    }
}
