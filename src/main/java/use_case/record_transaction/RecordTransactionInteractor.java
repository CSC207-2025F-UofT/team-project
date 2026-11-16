package use_case.record_transaction;

/**
 * The Record Transaction Interactor.
 */
public class RecordTransactionInteractor
        implements RecordTransactionInputBoundary {

    private final RecordTransactionDataAccessInterface transactionDataAccess;
    private final RecordTransactionOutputBoundary transactionPresenter;

    public RecordTransactionInteractor(
            RecordTransactionDataAccessInterface transactionDataAccess,
            RecordTransactionOutputBoundary transactionPresenter) {

        this.transactionDataAccess = transactionDataAccess;
        this.transactionPresenter = transactionPresenter;
    }

    @Override
    public void execute(RecordTransactionInputData inputData) {
        // TODO: Get the signed-in account ID.
        // TODO: Reject the request if nobody is signed in.
        // TODO: Parse and validate the amount and transaction type.
        // TODO: Create and save the transaction for that account.
        // TODO: Present success, or an error if validation or saving fails.

        throw new UnsupportedOperationException(
                "Record Transaction is not implemented yet.");
    }
}