package use_case.transactions;

import entity.Account;
import entity.Transaction;
import entity.TransactionType;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/** Coordinates account-scoped dashboard actions without knowing about Swing or JSON. */
public final class TransactionDashboardInteractor implements TransactionDashboardInputBoundary {
    private final TransactionDataAccessInterface dataAccess;
    private final TransactionDashboardOutputBoundary presenter;

    public TransactionDashboardInteractor(TransactionDataAccessInterface dataAccess,
            TransactionDashboardOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(TransactionDashboardInputData input) {
        try {
            switch (input.getAction()) {
                case LOAD_ACCOUNTS:
                    presenter.present(new TransactionDashboardOutputData(dataAccess.getAccounts(),
                        Collections.emptyList(), Collections.emptyList(), BigDecimal.ZERO, BigDecimal.ZERO, ""));
                    break;
                case LOAD_ACCOUNT:
                    presentAccount(input.getAccountId(), "");
                    break;
                case ADD_TRANSACTION:
                    Transaction transaction = new Transaction(0, input.getType(), input.getAmount(),
                        input.getDescription(), input.getDate());
                    dataAccess.addTransaction(input.getAccountId(), transaction);
                    presentAccount(input.getAccountId(), "Transaction added successfully.");
                    break;
                case DELETE_TRANSACTION:
                    if (dataAccess.deleteTransaction(input.getAccountId(), input.getType(), input.getTransactionId())) {
                        presentAccount(input.getAccountId(), "Transaction deleted successfully.");
                    } else {
                        presenter.presentError("Transaction could not be found.");
                    }
                    break;
                default:
                    presenter.presentError("Unsupported dashboard action.");
            }
        } catch (IOException | IllegalArgumentException exception) {
            presenter.presentError(exception.getMessage());
        }
    }

    private void presentAccount(int accountId, String message) throws IOException {
        List<Transaction> income = dataAccess.getTransactions(accountId, TransactionType.INCOME);
        List<Transaction> expenses = dataAccess.getTransactions(accountId, TransactionType.EXPENSE);
        presenter.present(new TransactionDashboardOutputData(null, income, expenses,
            total(income), total(expenses), message));
    }

    private BigDecimal total(List<Transaction> transactions) {
        BigDecimal sum = BigDecimal.ZERO;
        for (Transaction transaction : transactions) sum = sum.add(transaction.getAmount());
        return sum;
    }
}
