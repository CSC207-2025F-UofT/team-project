package entity;

import java.time.LocalDate;

/**
 * Money a member spent during a trip. The original amount and currency are kept, along with the
 * amount converted into the trip's home currency at the time it was logged.
 */
public class Expense {

  private final String id;
  private final String tripId;
  private final String payerUsername;
  private final String description;
  private final double amount;
  private final String currency;
  private final double amountInHomeCurrency;
  private final ExpenseCategory category;
  private final LocalDate date;

  /**
   * Creates an expense.
   *
   * @param id unique id of the expense
   * @param tripId the trip this expense belongs to
   * @param payerUsername the member who paid
   * @param description what the money was spent on
   * @param amount the amount paid, in the original currency
   * @param currency ISO 4217 code of the original currency
   * @param amountInHomeCurrency the amount converted into the trip's home currency
   * @param category the kind of spending
   * @param date the day the money was spent
   * @throws IllegalArgumentException if any field is missing or invalid
   */
  public Expense(
      String id,
      String tripId,
      String payerUsername,
      String description,
      double amount,
      String currency,
      double amountInHomeCurrency,
      ExpenseCategory category,
      LocalDate date) {
    this.id = Validation.requireNonBlank(id, "Expense id");
    this.tripId = Validation.requireNonBlank(tripId, "Trip id");
    this.payerUsername = Validation.requireNonBlank(payerUsername, "Payer");
    this.description = Validation.requireNonBlank(description, "Description");
    this.amount = Validation.requirePositive(amount, "Amount");
    this.currency = Validation.requireCurrencyCode(currency);
    this.amountInHomeCurrency =
        Validation.requirePositive(amountInHomeCurrency, "Converted amount");
    this.category = Validation.requireNonNull(category, "Category");
    this.date = Validation.requireNonNull(date, "Date");
  }

  public String getId() {
    return id;
  }

  public String getTripId() {
    return tripId;
  }

  public String getPayerUsername() {
    return payerUsername;
  }

  public String getDescription() {
    return description;
  }

  public double getAmount() {
    return amount;
  }

  public String getCurrency() {
    return currency;
  }

  public double getAmountInHomeCurrency() {
    return amountInHomeCurrency;
  }

  public ExpenseCategory getCategory() {
    return category;
  }

  public LocalDate getDate() {
    return date;
  }
}
