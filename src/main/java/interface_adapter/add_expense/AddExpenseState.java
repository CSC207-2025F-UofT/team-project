package interface_adapter.add_expense;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * View state held by {@link AddExpenseViewModel} and rendered by {@link view.AddExpenseView}.
 */
public class AddExpenseState {

    /**
     * Formatted display row representing a single {@link entity.Expense} in the expenses table.
     */
    public static class ExpenseRow {
        private final String description;
        private final String payer;
        private final String amountInHomeCurrency;

        /**
         * Creates a table row for an expense.
         *
         * @param description formatted description, including original foreign amount if applicable
         * @param payer username of the member who paid
         * @param amountInHomeCurrency formatted amount in the trip's home currency
         */
        public ExpenseRow(String description, String payer, String amountInHomeCurrency) {
            this.description = description;
            this.payer = payer;
            this.amountInHomeCurrency = amountInHomeCurrency;
        }

        /**
         * Returns the formatted description text.
         *
         * @return the description column value
         */
        public String getDescription() {
            return description;
        }

        /**
         * Returns the payer's username.
         *
         * @return the paid-by column value
         */
        public String getPayer() {
            return payer;
        }

        /**
         * Returns the formatted home-currency amount.
         *
         * @return the home-currency column value
         */
        public String getAmountInHomeCurrency() {
            return amountInHomeCurrency;
        }
    }

    private String tripId = "";
    private String tripName = "";
    private String homeCurrency = "CAD";
    private List<ExpenseRow> expenses = new ArrayList<>();
    private String statusMessage = "";
    private boolean error = false;
    private boolean clearForm = false;

    /**
     * Returns the id of the active trip.
     *
     * @return the trip id
     */
    public String getTripId() {
        return tripId;
    }

    /**
     * Sets the id of the active trip.
     *
     * @param tripId the trip id
     */
    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    /**
     * Returns the display name of the active trip.
     *
     * @return the trip name
     */
    public String getTripName() {
        return tripName;
    }

    /**
     * Sets the display name of the active trip.
     *
     * @param tripName the trip name
     */
    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    /**
     * Returns the trip's home currency code.
     *
     * @return the ISO 4217 currency code
     */
    public String getHomeCurrency() {
        return homeCurrency;
    }

    /**
     * Sets the trip's home currency code.
     *
     * @param homeCurrency the ISO 4217 currency code
     */
    public void setHomeCurrency(String homeCurrency) {
        this.homeCurrency = homeCurrency;
    }

    /**
     * Returns the formatted expense rows to display in the table.
     *
     * @return an unmodifiable list of {@link ExpenseRow} items
     */
    public List<ExpenseRow> getExpenses() {
        return Collections.unmodifiableList(expenses);
    }

    /**
     * Replaces the formatted expense rows to display in the table.
     *
     * @param expenses the formatted {@link ExpenseRow} list
     */
    public void setExpenses(List<ExpenseRow> expenses) {
        this.expenses = new ArrayList<>(expenses);
    }

    /**
     * Returns the current success or error message shown beside the Save button.
     *
     * @return the status message
     */
    public String getStatusMessage() {
        return statusMessage;
    }

    /**
     * Sets the status message shown beside the Save button.
     *
     * @param statusMessage the status or error text
     */
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    /**
     * Returns whether {@link #getStatusMessage()} represents an error.
     *
     * @return {@code true} if the status is an error, {@code false} otherwise
     */
    public boolean isError() {
        return error;
    }

    /**
     * Sets whether the current status message represents an error.
     *
     * @param error {@code true} for an error message, {@code false} for success
     */
    public void setError(boolean error) {
        this.error = error;
    }

    /**
     * Returns whether the view should clear the input fields after this update.
     *
     * @return {@code true} to clear the form fields
     */
    public boolean isClearForm() {
        return clearForm;
    }

    /**
     * Sets whether the view should clear the input fields after this update.
     *
     * @param clearForm {@code true} to clear the form fields
     */
    public void setClearForm(boolean clearForm) {
        this.clearForm = clearForm;
    }
}