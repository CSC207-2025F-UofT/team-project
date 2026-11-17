package use_case.log_expense;

import entity.Expense;
import java.util.List;

/**
 * Output data passed from {@link LogExpenseInteractor} to {@link LogExpenseOutputBoundary} after
 * an expense is successfully saved.
 */
public class LogExpenseOutputData {

    private final String tripId;
    private final String tripName;
    private final String homeCurrency;
    private final List<Expense> expenses;

    /**
     * Creates output data containing the updated expense list for the trip.
     *
     * @param tripId the id of the {@link entity.Trip}
     * @param tripName the display name of the trip
     * @param homeCurrency the trip's home ISO 4217 currency code
     * @param expenses an unmodifiable copy of all {@link Expense} records for the trip
     */
    public LogExpenseOutputData(
            String tripId, String tripName, String homeCurrency, List<Expense> expenses) {
        this.tripId = tripId;
        this.tripName = tripName;
        this.homeCurrency = homeCurrency;
        this.expenses = List.copyOf(expenses);
    }

    /**
     * Returns the id of the trip.
     *
     * @return the trip id
     */
    public String getTripId() {
        return tripId;
    }

    /**
     * Returns the name of the trip.
     *
     * @return the trip name
     */
    public String getTripName() {
        return tripName;
    }

    /**
     * Returns the trip's home currency code.
     *
     * @return the ISO 4217 home currency code
     */
    public String getHomeCurrency() {
        return homeCurrency;
    }

    /**
     * Returns the updated list of expenses for the trip.
     *
     * @return an unmodifiable list of {@link Expense} objects
     */
    public List<Expense> getExpenses() {
        return expenses;
    }
}