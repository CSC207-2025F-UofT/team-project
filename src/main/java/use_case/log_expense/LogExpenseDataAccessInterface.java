package use_case.log_expense;

import entity.Expense;
import entity.Trip;
import java.util.List;

/**
 * Data access boundary for the Log Expense use case. Implemented by {@link
 * data_access.InMemoryTripDataAccessObject} and {@link data_access.JsonFileTripDataAccessObject}.
 */
public interface LogExpenseDataAccessInterface {

    /**
     * Returns the id of the trip currently open in the application.
     *
     * @return the open trip's id, or {@code null} if no trip is open
     */
    String getCurrentTripId();

    /**
     * Looks up a {@link Trip} by its unique identifier.
     *
     * @param tripId the trip id to look up
     * @return the matching {@link Trip}, or {@code null} if none exists
     */
    Trip getTrip(String tripId);

    /**
     * Persists a newly created {@link Expense}.
     *
     * @param expense the expense to save
     */
    void saveExpense(Expense expense);

    /**
     * Returns every {@link Expense} recorded for the given trip, in the order logged.
     *
     * @param tripId the id of the trip
     * @return the list of expenses belonging to that trip
     */
    List<Expense> getExpenses(String tripId);
}