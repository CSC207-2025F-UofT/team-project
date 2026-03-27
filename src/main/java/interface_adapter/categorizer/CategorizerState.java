package interface_adapter.categorizer;

import entity.Transaction;

import java.util.List;

public class CategorizerState {

    private List<Transaction> transactions;

    public void setTransactions(List<Transaction> t) {
        this.transactions = t;
    }

    public List<Transaction> getTransactions() {
        return this.transactions;
    }
}
