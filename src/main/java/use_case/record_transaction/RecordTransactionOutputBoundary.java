package use_case.record_transaction;

/**
 * Presents the result of recording a transaction.
 */
public interface RecordTransactionOutputBoundary {

    void prepareSuccessView();

    void prepareFailView(String error);
}