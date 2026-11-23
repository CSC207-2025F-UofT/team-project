package use_case.view_budget_progress;
import entity.Transaction;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDate;

public class ViewBudgetProgressInteractor implements ViewBudgetProgressInputBoundary {
    private final ViewBudgetProgressTransactionDataAccessInterface transactions;
    private final ViewBudgetProgressBudgetDataAccessInterface budgets;
    private final ViewBudgetProgressOutputBoundary presenter;
    private final Clock clock;

    public ViewBudgetProgressInteractor(ViewBudgetProgressTransactionDataAccessInterface transactions,
            ViewBudgetProgressBudgetDataAccessInterface budgets,
            ViewBudgetProgressOutputBoundary presenter) {
        this(transactions, budgets, presenter, Clock.systemUTC());
    }

    public ViewBudgetProgressInteractor(ViewBudgetProgressTransactionDataAccessInterface transactions,
            ViewBudgetProgressBudgetDataAccessInterface budgets,
            ViewBudgetProgressOutputBoundary presenter, Clock clock) {
        this.transactions = transactions;
        this.budgets = budgets;
        this.presenter = presenter;
        this.clock = clock;
    }

    @Override
    public void execute() {
        String username = transactions.getCurrentUsername();
        if (username == null || username.isBlank()) {
            presenter.prepareFailView("Please sign in to view budget progress.");
            return;
        }
        BigDecimal limit = budgets.getDailyLimit(username);
        if (limit == null) {
            presenter.prepareFailView("Set a daily budget first.");
            return;
        }
        if (limit.signum() <= 0) {
            presenter.prepareFailView("The daily budget must be greater than zero.");
            return;
        }
        LocalDate today = LocalDate.now(clock);
        BigDecimal spent = BigDecimal.ZERO;
        for (Transaction transaction : transactions.getTransactions(username)) {
            if (transaction.getType() == Transaction.Type.EXPENSE
                    && transaction.getDate().equals(today)) {
                spent = spent.add(transaction.getAmount());
            }
        }
        BigDecimal remaining = limit.subtract(spent);
        BigDecimal percentage = spent.multiply(new BigDecimal("100"))
                .divide(limit, 1, RoundingMode.HALF_UP);
        presenter.prepareSuccessView(new ViewBudgetProgressOutputData(
                today, limit, spent, remaining, percentage));
    }
}
