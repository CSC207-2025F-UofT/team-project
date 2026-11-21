package use_case.daily_budget;

import entity.Expense;
import entity.Trip;
import java.util.List;

/** Reads the trips and expenses needed by the daily budget use case. */
public interface DailyBudgetDataAccessInterface {
  List<Trip> getTripsForMember(String username);

  String getCurrentTripId();

  void setCurrentTripId(String tripId);

  Trip getTrip(String tripId);

  List<Expense> getExpenses(String tripId);

  /** Saves the budget, returning false if it could not be persisted. */
  boolean updateDailyBudget(Trip trip, double amount);
}
