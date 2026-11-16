package use_case.record_transaction;

import entity.Transaction;

/**
 * Data access operations required to record a transaction.
 */
public interface RecordTransactionDataAccessInterface {

    /**
     * Returns the signed-in account ID, or null if nobody is signed in.
     */
    String getCurrentUsername();

    /**
     * Saves a transaction belonging to the specified account.
     */
    void save(String accountId, Transaction transaction);
}