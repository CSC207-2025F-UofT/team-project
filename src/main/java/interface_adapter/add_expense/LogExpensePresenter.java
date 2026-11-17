package interface_adapter.add_expense;

import entity.Expense;
import interface_adapter.add_expense.AddExpenseState.ExpenseRow;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import use_case.log_expense.LogExpenseOutputBoundary;
import use_case.log_expense.LogExpenseOutputData;

/**
 * Presenter for Use Case 3: Log an expense. Formats {@link LogExpenseOutputData} or error messages
 * into {@link AddExpenseState} and notifies {@link AddExpenseViewModel}.
 */
public class LogExpensePresenter implements LogExpenseOutputBoundary {

    private final AddExpenseViewModel addExpenseViewModel;

    /**
     * Creates a {@link LogExpensePresenter}.
     *
     * @param addExpenseViewModel the view model updated by this presenter
     */
    public LogExpensePresenter(AddExpenseViewModel addExpenseViewModel) {
        this.addExpenseViewModel = addExpenseViewModel;
    }

    /**
     * Updates {@link AddExpenseViewModel} with the saved expense list, sets a confirmation status,
     * and signals the view to clear the form.
     *
     * @param outputData the updated trip expenses returned by {@link
     *     use_case.log_expense.LogExpenseInteractor}
     */
    @Override
    public void prepareSuccessView(LogExpenseOutputData outputData) {
        final AddExpenseState state = addExpenseViewModel.getState();
        state.setTripId(outputData.getTripId());
        state.setTripName(outputData.getTripName());
        state.setHomeCurrency(outputData.getHomeCurrency());
        state.setExpenses(formatExpenses(outputData.getExpenses(), outputData.getHomeCurrency()));
        state.setStatusMessage("Expense saved");
        state.setError(false);
        state.setClearForm(true);
        addExpenseViewModel.notifyStateChanged();
    }

    /**
     * Updates {@link AddExpenseViewModel} with an error message without clearing the user's form
     * inputs.
     *
     * @param errorMessage the failure reason to display
     */
    @Override
    public void prepareFailView(String errorMessage) {
        final AddExpenseState state = addExpenseViewModel.getState();
        state.setStatusMessage(errorMessage);
        state.setError(true);
        state.setClearForm(false);
        addExpenseViewModel.notifyStateChanged();
    }

    /**
     * Converts {@link Expense} entities into {@link ExpenseRow} display rows. Foreign-currency
     * expenses include the original amount and currency in parentheses (e.g., {@code Taxi (9,000
     * JPY)}).
     *
     * @param expenses the trip's expenses
     * @param homeCurrency the trip's home currency code
     * @return formatted rows ready for display in {@link view.AddExpenseView}
     */
    public static List<ExpenseRow> formatExpenses(List<Expense> expenses, String homeCurrency) {
        final DecimalFormat moneyFormat = new DecimalFormat("#,##0.00");
        final DecimalFormat wholeOrDecimal = new DecimalFormat("#,##0.##");
        final List<ExpenseRow> rows = new ArrayList<>();
        for (Expense expense : expenses) {
            final String desc;
            if (!expense.getCurrency().equalsIgnoreCase(homeCurrency)) {
                desc =
                        expense.getDescription()
                                + " ("
                                + wholeOrDecimal.format(expense.getAmount())
                                + " "
                                + expense.getCurrency()
                                + ")";
            } else {
                desc = expense.getDescription();
            }
            final String homeAmount = "$" + moneyFormat.format(expense.getAmountInHomeCurrency());
            rows.add(new ExpenseRow(desc, expense.getPayerUsername(), homeAmount));
        }
        return rows;
    }
}