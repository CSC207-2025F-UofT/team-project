package interface_adapter.daily_budget;

import java.util.List;
import use_case.daily_budget.DailyBudgetOutputData;
import use_case.daily_budget.DailyBudgetTripOption;

/** The visible trip choices, last valid summary, and feedback. */
public final class DailyBudgetState {
  private final List<DailyBudgetTripOption> trips;
  private final DailyBudgetOutputData summary;
  private final String message;
  private final boolean error;

  DailyBudgetState(
      List<DailyBudgetTripOption> trips, DailyBudgetOutputData summary,
      String message, boolean error) {
    this.trips = List.copyOf(trips);
    this.summary = summary;
    this.message = message;
    this.error = error;
  }

  public List<DailyBudgetTripOption> getTrips() {
    return trips;
  }

  public DailyBudgetOutputData getSummary() {
    return summary;
  }

  public String getMessage() {
    return message;
  }

  public boolean hasError() {
    return error;
  }
}
