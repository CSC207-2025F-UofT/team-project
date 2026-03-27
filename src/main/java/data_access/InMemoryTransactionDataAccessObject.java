package data_access;
import entity.Transaction;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class InMemoryTransactionDataAccessObject {
    private final List<Transaction> transactions = new ArrayList<>();

    public void saveTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public List<Transaction> getAllTransactions() {
        return transactions;
    }

    public void clearAllTransactions() {
        transactions.clear();
    }

    public List<Transaction> getTransactionsByCategory(String category) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction transaction : this.transactions) {
            if (transaction.getCategory().equals(category)) {
                result.add(transaction);
            }
        }
        return result;
    }

    public Map<String, Float> getTotalSpendByCategory() {
        HashMap<String, Float> result = new HashMap<>();
        for (Transaction transaction : this.transactions) {
            if (result.containsKey(transaction.getCategory())) {
                // result.put(transaction.getCategory(), result.get(transaction.getCategory()) + transaction.getAmount());
            } else {
                // result.put(transaction.getCategory(), transaction.getAmount());
            }
        }
        return result;
    }


}
