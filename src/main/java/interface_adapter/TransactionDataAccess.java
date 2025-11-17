package interface_adapter;

import java.util.List;

import entity.Transaction;

public interface TransactionDataAccess {
    List<Transaction> getTransactions(int userId, String month);
}