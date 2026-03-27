package interface_adapter.filter_search;

import entity.Transaction;
import java.util.List;

public class FilterSearchState {
    private List<Transaction> filtered;
    private String error;

    public List<Transaction> getFiltered() {
        return filtered;
    }

    public void setFiltered(List<Transaction> filtered) {
        this.filtered = filtered;
    }

    public String getError() { return error; }

    public void setError(String error) {
        this.error = error;
    }
}
