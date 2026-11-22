package application_business_rules.transactions;

import entity.Transaction;
import entity.TransactionType;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/** Coordinates dashboard actions and calculates totals, independent of Swing and JSON. */
public final class TransactionDashboardInteractor implements TransactionDashboardInputBoundary {
    private final TransactionDataAccessInterface dataAccess;
    private final TransactionDashboardOutputBoundary presenter;

    public TransactionDashboardInteractor(TransactionDataAccessInterface dataAccess,
            TransactionDashboardOutputBoundary presenter) {
        this.dataAccess = dataAccess; this.presenter = presenter;
    }

    @Override
    public void execute(TransactionDashboardInputData input) {
        try {
            switch (input.getAction()) {
                case LOAD_USERS:
                    presenter.present(new TransactionDashboardOutputData(dataAccess.getUserNames(),
                        Collections.emptyList(), Collections.emptyList(), BigDecimal.ZERO, BigDecimal.ZERO, ""));
                    break;
                case LOAD_USER:
                    presentUser(input.getUserName(), "");
                    break;
                case ADD_TRANSACTION:
                    dataAccess.addTransaction(input.getUserName(), input.getTransaction());
                    presentUser(input.getUserName(), "Transaction added successfully.");
                    break;
                case DELETE_TRANSACTION:
                    if (dataAccess.deleteTransaction(input.getUserName(), input.getType(), input.getTransactionId())) {
                        presentUser(input.getUserName(), "Transaction deleted successfully.");
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

    private void presentUser(String userName, String message) throws IOException {
        List<Transaction> income = dataAccess.getTransactions(userName, TransactionType.INCOME);
        List<Transaction> expenses = dataAccess.getTransactions(userName, TransactionType.EXPENSE);
        presenter.present(new TransactionDashboardOutputData(Collections.emptyList(), income, expenses,
            total(income), total(expenses), message));
    }

    private BigDecimal total(List<Transaction> transactions) {
        BigDecimal sum = BigDecimal.ZERO;
        for (Transaction transaction : transactions) sum = sum.add(transaction.getAmount());
        return sum;
    }
}
