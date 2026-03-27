package interface_adapter;

import java.util.List;

import entity.Transaction;

public interface TransactionDataAccess {
    void saveTransaction(Transaction transaction);
    List<Transaction> getTransactions(String username);
    List<Transaction> getAllTransactions();
}