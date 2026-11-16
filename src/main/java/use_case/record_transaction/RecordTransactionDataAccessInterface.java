package use_case.record_transaction;

import entity.Transaction;

import java.io.IOException;

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
     *
     * @throws IOException if the transaction cannot be saved
     */
    void save(String accountId, Transaction transaction) throws IOException;
}