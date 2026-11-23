package use_case.record_transaction;

/**
 * Entry point for the Record Transaction Use Case.
 */
public interface RecordTransactionInputBoundary {

    void execute(RecordTransactionInputData inputData);
}