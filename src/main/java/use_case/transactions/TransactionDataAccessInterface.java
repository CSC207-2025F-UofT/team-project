package use_case.transactions;

import entity.Transaction;
import entity.TransactionType;
import java.io.IOException;
import java.util.List;

/** Data Access Interface owned by the use-case layer. */
public interface TransactionDataAccessInterface {
    List<String> getUserNames() throws IOException;
    List<Transaction> getTransactions(String userName, TransactionType type) throws IOException;
    void addTransaction(String userName, Transaction transaction) throws IOException;
    boolean deleteTransaction(String userName, TransactionType type, int transactionId) throws IOException;
}
