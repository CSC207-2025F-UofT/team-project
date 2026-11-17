package use_case.log_expense;

import entity.ExpenseCategory;

/**
 * Immutable input data passed from {@link interface_adapter.add_expense.LogExpenseController} to
 * {@link LogExpenseInteractor}. Numeric and date fields are carried as raw text so the interactor
 * can validate formatting and enforce all Use Case 3 rules.
 */
public class LogExpenseInputData {

    private final String tripId;
    private final String description;
    private final String amountText;
    private final String currency;
    private final ExpenseCategory category;
    private final String dateText;
    private final String payerUsername;

    /**
     * Creates input data for logging an expense.
     *
     * @param tripId the id of the trip, or blank to fall back to the currently open trip
     * @param description what the money was spent on
     * @param amountText the amount paid as entered by the user
     * @param currency the ISO 4217 currency code the expense was paid in
     * @param category the {@link ExpenseCategory} for the expense
     * @param dateText the expense date in {@code YYYY-MM-DD} format
     * @param payerUsername the username of the {@link entity.User} who paid
     */
    public LogExpenseInputData(
            String tripId,
            String description,
            String amountText,
            String currency,
            ExpenseCategory category,
            String dateText,
            String payerUsername) {
        this.tripId = tripId;
        this.description = description;
        this.amountText = amountText;
        this.currency = currency;
        this.category = category;
        this.dateText = dateText;
        this.payerUsername = payerUsername;
    }

    /**
     * Returns the target trip id.
     *
     * @return the trip id
     */
    public String getTripId() {
        return tripId;
    }

    /**
     * Returns the expense description.
     *
     * @return the description text
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the raw amount string entered by the user.
     *
     * @return the amount text
     */
    public String getAmountText() {
        return amountText;
    }

    /**
     * Returns the currency code entered by the user.
     *
     * @return the currency code
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Returns the selected spending category.
     *
     * @return the {@link ExpenseCategory}
     */
    public ExpenseCategory getCategory() {
        return category;
    }

    /**
     * Returns the raw date string entered by the user.
     *
     * @return the date text
     */
    public String getDateText() {
        return dateText;
    }

    /**
     * Returns the username of the member who paid.
     *
     * @return the payer's username
     */
    public String getPayerUsername() {
        return payerUsername;
    }
}