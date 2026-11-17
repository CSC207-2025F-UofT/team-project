package use_case.log_expense;

/**
 * Output boundary for Use Case 3: Log an expense. Implemented by {@link
 * interface_adapter.add_expense.LogExpensePresenter}.
 */
public interface LogExpenseOutputBoundary {

    /**
     * Prepares the view model when the expense has been validated, converted, and saved.
     *
     * @param outputData the {@link LogExpenseOutputData} containing the updated expense list
     */
    void prepareSuccessView(LogExpenseOutputData outputData);

    /**
     * Prepares the view model when validation or exchange rate lookup fails.
     *
     * @param errorMessage a user-facing message explaining why nothing was recorded
     */
    void prepareFailView(String errorMessage);
}