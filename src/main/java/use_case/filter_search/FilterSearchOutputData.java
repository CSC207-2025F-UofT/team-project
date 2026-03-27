package use_case.filter_search;

import entity.Transaction;
import java.util.List;

public class FilterSearchOutputData {

    private final List<Transaction> filteredTransactions;

    public FilterSearchOutputData(List<Transaction> filteredTransactions) {
        this.filteredTransactions = filteredTransactions;
    }

    public List<Transaction> getFilteredTransactions() {
        return filteredTransactions;
    }
}
