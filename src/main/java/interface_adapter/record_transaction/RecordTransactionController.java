package interface_adapter.record_transaction;

import entity.Transaction;
import use_case.record_transaction.RecordTransactionInputBoundary;
import use_case.record_transaction.RecordTransactionInputData;

/**
 * The controller for the Record Transaction Use Case.
 */
public class RecordTransactionController {

    private final RecordTransactionInputBoundary recordTransactionInteractor;

    public RecordTransactionController(
            RecordTransactionInputBoundary recordTransactionInteractor) {
        this.recordTransactionInteractor = recordTransactionInteractor;
    }

    public void execute(String amount, Transaction.Type type) {
        final RecordTransactionInputData inputData =
                new RecordTransactionInputData(amount, type);

        recordTransactionInteractor.execute(inputData);
    }
}