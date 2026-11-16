package use_case.record_transaction;

import entity.Transaction;

import java.io.IOException;
import java.math.BigDecimal;

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
        final String accountId = transactionDataAccess.getCurrentUsername();

        if (accountId == null || accountId.isBlank()) {
            transactionPresenter.prepareFailView(
                    "Please sign in before recording a transaction.");
            return;
        }

        if (inputData == null || inputData.getAmount() == null
                || inputData.getAmount().isBlank()) {
            transactionPresenter.prepareFailView("Please enter an amount.");
            return;
        }

        final BigDecimal amount;
        try {
            amount = new BigDecimal(inputData.getAmount().strip());
        }
        catch (NumberFormatException exception) {
            transactionPresenter.prepareFailView(
                    "Please enter a valid number for the amount.");
            return;
        }

        final Transaction transaction;
        try {
            transaction = new Transaction(amount, inputData.getType());
        }
        catch (IllegalArgumentException exception) {
            transactionPresenter.prepareFailView(exception.getMessage());
            return;
        }

        try {
            transactionDataAccess.save(accountId, transaction);
        }
        catch (IOException exception) {
            transactionPresenter.prepareFailView(
                    "Could not save the transaction. Please try again.");
            return;
        }

        transactionPresenter.prepareSuccessView();
    }
}