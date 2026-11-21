package use_case.daily_budget;

import java.time.LocalDate;

/** The information needed to choose a trip and enter one of its dates. */
public final class DailyBudgetTripOption {
  private final String id;
  private final String label;
  private final LocalDate start;
  private final LocalDate end;

  public DailyBudgetTripOption(String id, String label, LocalDate start, LocalDate end) {
    this.id = id;
    this.label = label;
    this.start = start;
    this.end = end;
  }

  public String getId() {
    return id;
  }

  public LocalDate getStart() {
    return start;
  }

  public LocalDate getEnd() {
    return end;
  }

  @Override
  public String toString() {
    return label;
  }
}
