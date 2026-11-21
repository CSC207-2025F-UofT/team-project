package use_case.daily_budget;

import java.time.LocalDate;

/** Identifies the trip and day for a budget summary. */
public final class DailyBudgetInputData {
  private final String tripId;
  private final LocalDate date;

  public DailyBudgetInputData(String tripId, LocalDate date) {
    this.tripId = tripId;
    this.date = date;
  }

  public String getTripId() {
    return tripId;
  }

  public LocalDate getDate() {
    return date;
  }
}
