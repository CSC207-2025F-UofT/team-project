package use_case.daily_budget;

/** Opens the budget screen, shows a day, or changes the daily budget. */
public interface DailyBudgetInputBoundary {
  void open();

  void execute(DailyBudgetInputData input);

  void changeBudget(ChangeBudgetInputData input);
}
