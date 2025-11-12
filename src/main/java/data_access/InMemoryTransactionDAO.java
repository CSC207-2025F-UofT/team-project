package data_access;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.Transaction;

interface TransactionDataAccess {
    List<Transaction> getTransactions(int userId, String month);
}

public class InMemoryTransactionDAO implements TransactionDataAccess {
    private final Map<Integer, List<Transaction>> transactions = new HashMap<>();

    public InMemoryTransactionDAO() {
        // fake data for testing
        transactions.put(1, Arrays.asList(
            new Transaction(1, 1, "2025-09-02", "Walmart", 120.50f, "Groceries"),
            new Transaction(2, 1, "2025-09-10", "Starbucks", 15.00f, "Dining"),
            new Transaction(3, 1, "2025-09-20", "Amazon", 85.00f, "Shopping")
        ));
    }

    @Override
    public List<Transaction> getTransactions(int userId, String month) {
        List<Transaction> all = new ArrayList<>();
        for (List<Transaction> list : transactions.values()) {
            all.addAll(list);
        }
        return all;
    }
}
