package use_case.daily_budget;

import java.util.List;

/** Sends trip choices, calculated summaries, and errors to the presenter. */
public interface DailyBudgetOutputBoundary {
  void showTrips(List<DailyBudgetTripOption> trips);

  void showSummary(DailyBudgetOutputData summary, boolean budgetUpdated);

  void showError(String message);
}
