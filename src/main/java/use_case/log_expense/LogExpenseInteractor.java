package use_case.log_expense;

import entity.Expense;
import entity.ExpenseCategory;
import entity.Trip;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Currency;
import java.util.List;
import java.util.UUID;
import use_case.exchange_rate.ExchangeRateDataAccessInterface;
import use_case.exchange_rate.ExchangeRateUnavailableException;

/**
 * Interactor for Use Case 3: Log an expense. Validates expense inputs against the open {@link Trip},
 * converts foreign currencies via {@link ExchangeRateDataAccessInterface}, and saves the resulting
 * {@link Expense} through {@link LogExpenseDataAccessInterface}.
 */
public class LogExpenseInteractor implements LogExpenseInputBoundary {

    private static final double CENTS_PER_UNIT = 100.0;

    private final LogExpenseDataAccessInterface tripDataAccess;
    private final ExchangeRateDataAccessInterface exchangeRateDataAccess;
    private final LogExpenseOutputBoundary presenter;

    /**
     * Creates a {@link LogExpenseInteractor}.
     *
     * @param tripDataAccess data access for trips and expenses
     * @param exchangeRateDataAccess gateway for current exchange rates
     * @param presenter output boundary for presenting success or failure
     */
    public LogExpenseInteractor(
            LogExpenseDataAccessInterface tripDataAccess,
            ExchangeRateDataAccessInterface exchangeRateDataAccess,
            LogExpenseOutputBoundary presenter) {
        this.tripDataAccess = tripDataAccess;
        this.exchangeRateDataAccess = exchangeRateDataAccess;
        this.presenter = presenter;
    }

    /**
     * Executes Use Case 3: validates description, positive numeric amount, trip date range, payer
     * membership, and currency; converts to the trip's home currency if needed; and saves the
     * expense.
     *
     * @param inputData the raw expense fields submitted by the user
     */
    @Override
    public void execute(LogExpenseInputData inputData) {
        final String tripId =
                isBlank(inputData.getTripId())
                        ? tripDataAccess.getCurrentTripId()
                        : inputData.getTripId().trim();
        final Trip trip = isBlank(tripId) ? null : tripDataAccess.getTrip(tripId);
        if (trip == null) {
            presenter.prepareFailView("No trip is currently open.");
            return;
        }

        // 2a. Check description and amount.
        final String description = inputData.getDescription();
        if (isBlank(description)) {
            presenter.prepareFailView("Description cannot be empty.");
            return;
        }

        final double amount;
        try {
            if (isBlank(inputData.getAmountText())) {
                presenter.prepareFailView("Amount must be a number.");
                return;
            }
            amount = Double.parseDouble(inputData.getAmountText().trim());
        } catch (NumberFormatException ex) {
            presenter.prepareFailView("Amount must be a number.");
            return;
        }
        if (!Double.isFinite(amount) || amount <= 0) {
            presenter.prepareFailView("Amount must be greater than 0.");
            return;
        }

        // 2b. Check date format and that it falls within the trip's dates.
        final LocalDate date;
        try {
            if (isBlank(inputData.getDateText())) {
                presenter.prepareFailView("Date must be in YYYY-MM-DD format.");
                return;
            }
            date = LocalDate.parse(inputData.getDateText().trim());
        } catch (DateTimeParseException ex) {
            presenter.prepareFailView("Date must be in YYYY-MM-DD format.");
            return;
        }
        if (!trip.containsDate(date)) {
            presenter.prepareFailView("That date is not part of this trip.");
            return;
        }

        // 2c. Check that the payer is a member of the trip.
        final String payer =
                inputData.getPayerUsername() == null ? "" : inputData.getPayerUsername().trim();
        if (payer.isEmpty() || !trip.hasMember(payer)) {
            presenter.prepareFailView(
                    payer.isEmpty()
                            ? "Paid by cannot be empty."
                            : payer + " is not a member of this trip.");
            return;
        }

        // 2d. Check currency code.
        if (isBlank(inputData.getCurrency())) {
            presenter.prepareFailView("Currency cannot be empty.");
            return;
        }
        final String currency = inputData.getCurrency().trim().toUpperCase();
        try {
            Currency.getInstance(currency);
        } catch (IllegalArgumentException ex) {
            presenter.prepareFailView("Currency " + currency + " is not supported.");
            return;
        }

        final ExpenseCategory category =
                inputData.getCategory() == null ? ExpenseCategory.OTHER : inputData.getCategory();

        // 3. Convert to home currency (3a if same currency, 3b if rate unavailable).
        final double amountInHomeCurrency;
        if (currency.equals(trip.getHomeCurrency())) {
            amountInHomeCurrency = roundToCent(amount);
        } else {
            final double rate;
            try {
                rate = exchangeRateDataAccess.getRate(currency, trip.getHomeCurrency());
            } catch (ExchangeRateUnavailableException ex) {
                if (ex.getMessage() != null && ex.getMessage().contains("not supported")) {
                    presenter.prepareFailView("Currency " + currency + " is not supported.");
                } else {
                    presenter.prepareFailView("Couldn't get the exchange rate. Try again.");
                }
                return;
            }
            amountInHomeCurrency = roundToCent(amount * rate);
        }

        if (amountInHomeCurrency <= 0) {
            presenter.prepareFailView("Converted amount must be greater than 0.");
            return;
        }

        // 4. Record the expense.
        final Expense expense =
                new Expense(
                        UUID.randomUUID().toString(),
                        trip.getId(),
                        payer,
                        description.trim(),
                        amount,
                        currency,
                        amountInHomeCurrency,
                        category,
                        date);
        tripDataAccess.saveExpense(expense);

        // 5. Present updated expenses.
        final List<Expense> expenses = tripDataAccess.getExpenses(trip.getId());
        presenter.prepareSuccessView(
                new LogExpenseOutputData(trip.getId(), trip.getName(), trip.getHomeCurrency(), expenses));
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private static double roundToCent(double value) {
        return Math.round(value * CENTS_PER_UNIT) / CENTS_PER_UNIT;
    }
}