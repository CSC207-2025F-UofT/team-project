package interface_adapter.add_expense;

import entity.ExpenseCategory;
import use_case.log_expense.LogExpenseInputBoundary;
import use_case.log_expense.LogExpenseInputData;

/**
 * Controller for Use Case 3: Log an expense. Translates Swing form values from {@link
 * view.AddExpenseView} into {@link LogExpenseInputData} and delegates to {@link
 * LogExpenseInputBoundary}.
 */
public class LogExpenseController {

    private final LogExpenseInputBoundary logExpenseInteractor;

    /**
     * Creates a {@link LogExpenseController}.
     *
     * @param logExpenseInteractor the input boundary for logging an expense
     */
    public LogExpenseController(LogExpenseInputBoundary logExpenseInteractor) {
        this.logExpenseInteractor = logExpenseInteractor;
    }

    /**
     * Executes the Log Expense use case with the user's form inputs.
     *
     * @param tripId the open trip id
     * @param description what the money was spent on
     * @param amountText the amount paid as entered in the form
     * @param currency the ISO 4217 currency code
     * @param category the selected {@link ExpenseCategory}
     * @param dateText the date string in {@code YYYY-MM-DD} format
     * @param payerUsername the username of the member who paid
     */
    public void execute(
            String tripId,
            String description,
            String amountText,
            String currency,
            ExpenseCategory category,
            String dateText,
            String payerUsername) {
        final LogExpenseInputData inputData =
                new LogExpenseInputData(
                        tripId, description, amountText, currency, category, dateText, payerUsername);
        logExpenseInteractor.execute(inputData);
    }
}