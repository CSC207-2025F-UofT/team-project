package data_access;

import entity.Transaction;
import use_case.login.LoginUserDataAccessInterface;
import use_case.record_transaction.RecordTransactionDataAccessInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores transactions per account for the current application run.
 */
public class InMemoryTransactionDataAccessObject
        implements RecordTransactionDataAccessInterface {

    private final LoginUserDataAccessInterface userDataAccess;
    private final Map<String, List<Transaction>> transactions = new HashMap<>();

    public InMemoryTransactionDataAccessObject(
            LoginUserDataAccessInterface userDataAccess) {
        this.userDataAccess = userDataAccess;
    }

    @Override
    public String getCurrentUsername() {
        return userDataAccess.getCurrentUsername();
    }

    @Override
    public void save(String accountId, Transaction transaction) {
        transactions.computeIfAbsent(accountId, key -> new ArrayList<>())
                .add(transaction);
    }

    /**
     * Returns a copy of the account's transactions.
     */
    public List<Transaction> getTransactions(String accountId) {
        return new ArrayList<>(
                transactions.getOrDefault(accountId, Collections.emptyList()));
    }
}