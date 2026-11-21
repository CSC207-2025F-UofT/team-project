package interface_adapter.daily_budget;

import java.util.List;
import use_case.daily_budget.DailyBudgetOutputBoundary;
import use_case.daily_budget.DailyBudgetOutputData;
import use_case.daily_budget.DailyBudgetTripOption;

/** Updates the budget screen after a request succeeds or fails. */
public final class DailyBudgetPresenter implements DailyBudgetOutputBoundary {
  private final DailyBudgetViewModel model;

  public DailyBudgetPresenter(DailyBudgetViewModel model) {
    this.model = model;
  }

  @Override
  public void showTrips(List<DailyBudgetTripOption> trips) {
    show(new DailyBudgetState(trips, null, "", false));
  }

  @Override
  public void showSummary(DailyBudgetOutputData summary, boolean budgetUpdated) {
    final String message = budgetUpdated ? "Daily budget updated." : "";
    show(new DailyBudgetState(model.getState().getTrips(), summary, message, false));
  }

  @Override
  public void showError(String message) {
    final DailyBudgetState previous = model.getState();
    show(new DailyBudgetState(previous.getTrips(), previous.getSummary(), message, true));
  }

  private void show(DailyBudgetState state) {
    model.setState(state);
    model.notifyStateChanged();
  }
}
