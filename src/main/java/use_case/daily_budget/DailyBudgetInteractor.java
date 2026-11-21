package use_case.daily_budget;

import entity.Expense;
import entity.ExpenseCategory;
import entity.Trip;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/** Views a trip day's budget and lets trip members change the daily budget. */
public final class DailyBudgetInteractor implements DailyBudgetInputBoundary {
  private final DailyBudgetDataAccessInterface trips;
  private final Supplier<String> currentUsername;
  private final DailyBudgetOutputBoundary presenter;

  public DailyBudgetInteractor(
      DailyBudgetDataAccessInterface trips,
      Supplier<String> currentUsername,
      DailyBudgetOutputBoundary presenter) {
    this.trips = trips;
    this.currentUsername = currentUsername;
    this.presenter = presenter;
  }

  @Override
  public void open() {
    final String username = currentUsername.get();
    final List<DailyBudgetTripOption> options = new ArrayList<>();
    if (username != null) {
      final String currentTripId = trips.getCurrentTripId();
      for (Trip trip : trips.getTripsForMember(username)) {
        final DailyBudgetTripOption option = new DailyBudgetTripOption(
            trip.getId(), trip.getName() + " — " + trip.getDestination(),
            trip.getStartDate(), trip.getEndDate());
        if (trip.getId().equals(currentTripId)) {
          options.add(0, option);
        } else {
          options.add(option);
        }
      }
    }
    presenter.showTrips(options);
  }

  @Override
  public void execute(DailyBudgetInputData input) {
    final Trip trip = validateDay(input);
    if (trip != null) {
      trips.setCurrentTripId(trip.getId());
      presenter.showSummary(summarize(trip, input.getDate()), false);
    }
  }

  @Override
  public void changeBudget(ChangeBudgetInputData input) {
    final Trip trip = validateDay(input.getDay());
    if (trip == null) {
      return;
    }

    final double budget;
    try {
      final String rawAmount = input.getAmount();
      final BigDecimal amount = new BigDecimal(rawAmount == null ? "" : rawAmount.strip());
      budget = amount.doubleValue();
      if (amount.signum() < 0 || !Double.isFinite(budget)) {
        throw new NumberFormatException();
      }
    } catch (NumberFormatException exception) {
      presenter.showError("Daily budget must be a number (0 or more).");
      return;
    }

    if (!trips.updateDailyBudget(trip, budget)) {
      presenter.showError("Could not save the daily budget. Please try again.");
      return;
    }
    presenter.showSummary(summarize(trip, input.getDay().getDate()), true);
  }

  private Trip validateDay(DailyBudgetInputData input) {
    final String username = currentUsername.get();
    final Trip trip = trips.getTrip(input.getTripId());
    if (username == null || trip == null || !trip.hasMember(username)) {
      presenter.showError("Choose a trip you belong to.");
      return null;
    }
    if (!trip.containsDate(input.getDate())) {
      presenter.showError("Choose a date within the trip.");
      return null;
    }
    return trip;
  }

  private DailyBudgetOutputData summarize(Trip trip, LocalDate date) {
    final BigDecimal budget = BigDecimal.valueOf(trip.getDailyBudget());
    final Map<ExpenseCategory, BigDecimal> categories = new EnumMap<>(ExpenseCategory.class);
    BigDecimal spentBefore = BigDecimal.ZERO;
    BigDecimal spentToday = BigDecimal.ZERO;

    for (Expense expense : trips.getExpenses(trip.getId())) {
      final LocalDate expenseDate = expense.getDate();
      final BigDecimal amount = BigDecimal.valueOf(expense.getAmountInHomeCurrency());
      if (expenseDate.equals(date)) {
        spentToday = spentToday.add(amount);
        categories.merge(expense.getCategory(), amount, BigDecimal::add);
      } else if (expenseDate.isBefore(date) && trip.containsDate(expenseDate)) {
        spentBefore = spentBefore.add(amount);
      }
    }

    final long earlierDays = ChronoUnit.DAYS.between(trip.getStartDate(), date);
    final BigDecimal tripDays =
        BigDecimal.valueOf(ChronoUnit.DAYS.between(trip.getStartDate(), trip.getEndDate()) + 1);
    final BigDecimal carryOver = budget.multiply(BigDecimal.valueOf(earlierDays)).subtract(spentBefore);
    final BigDecimal projectedCost = spentBefore.add(spentToday).multiply(tripDays)
        .divide(BigDecimal.valueOf(earlierDays + 1), MathContext.DECIMAL128);

    return new DailyBudgetOutputData(
        date, trip.getHomeCurrency(), budget, carryOver, spentToday, categories,
        projectedCost, budget.multiply(tripDays));
  }
}
