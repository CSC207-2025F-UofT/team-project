package use_case.daily_budget;

/** A new daily budget and the day whose summary should be refreshed. */
public final class ChangeBudgetInputData {
  private final DailyBudgetInputData day;
  private final String amount;

  public ChangeBudgetInputData(DailyBudgetInputData day, String amount) {
    this.day = day;
    this.amount = amount;
  }

  public DailyBudgetInputData getDay() {
    return day;
  }

  public String getAmount() {
    return amount;
  }
}
