package interface_adapter.daily_budget;

import java.time.LocalDate;
import use_case.daily_budget.ChangeBudgetInputData;
import use_case.daily_budget.DailyBudgetInputBoundary;
import use_case.daily_budget.DailyBudgetInputData;

/** Converts choices from the budget screen into use case requests. */
public final class DailyBudgetController {
  private final DailyBudgetInputBoundary interactor;

  public DailyBudgetController(DailyBudgetInputBoundary interactor) {
    this.interactor = interactor;
  }

  public void open() {
    interactor.open();
  }

  public void showDay(String tripId, LocalDate date) {
    interactor.execute(new DailyBudgetInputData(tripId, date));
  }

  public void changeBudget(String tripId, LocalDate date, String amount) {
    final DailyBudgetInputData day = new DailyBudgetInputData(tripId, date);
    interactor.changeBudget(new ChangeBudgetInputData(day, amount));
  }
}
