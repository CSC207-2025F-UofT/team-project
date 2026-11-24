package use_case.transactions;

import entity.Account;
import entity.Transaction;
import entity.TransactionType;
import java.io.IOException;
import java.util.List;

/** Data Access Interface owned by the use-case layer. */
public interface TransactionDataAccessInterface {
    List<Account> getAccounts() throws IOException;
    List<Transaction> getTransactions(int accountId, TransactionType type) throws IOException;
    void addTransaction(int accountId, Transaction transaction) throws IOException;
    boolean deleteTransaction(int accountId, TransactionType type, int transactionId) throws IOException;
}
