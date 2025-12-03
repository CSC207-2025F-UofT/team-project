package interface_adapter;

import entity.Transaction;
import java.util.List;

public class CategorizerViewModel {
    private List<Transaction> categorizedTransactions;

    public List<Transaction> getCategorizedTransactions() {
        return categorizedTransactions;
    }

    public void setCategorizedTransactions(List<Transaction> categorizedTransactions) {
        this.categorizedTransactions = categorizedTransactions;
    }
}
