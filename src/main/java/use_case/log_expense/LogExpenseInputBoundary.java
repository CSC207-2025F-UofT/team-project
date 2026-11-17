package use_case.log_expense;

/**
 * Input boundary for Use Case 3: Log an expense. Implemented by {@link LogExpenseInteractor} and
 * invoked by {@link interface_adapter.add_expense.LogExpenseController}.
 */
public interface LogExpenseInputBoundary {

    /**
     * Validates the submitted expense, converts foreign currency amounts into the trip's home
     * currency, persists the expense, and notifies the output boundary.
     *
     * @param inputData the {@link LogExpenseInputData} containing the user's expense fields
     */
    void execute(LogExpenseInputData inputData);
}