package use_case.daily_budget;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import data_access.InMemoryTripDataAccessObject;
import data_access.JsonFileTripDataAccessObject;
import entity.Expense;
import entity.ExpenseCategory;
import entity.Trip;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class DailyBudgetInteractorTest {
  private static final LocalDate START = LocalDate.of(2027, 2, 13);
  private static final String TRIP_ID = "tokyo";
  private final InMemoryTripDataAccessObject storage = new InMemoryTripDataAccessObject();
  private final RecordingPresenter presenter = new RecordingPresenter();
  private String username = "alex";
  private final DailyBudgetInteractor interactor =
      new DailyBudgetInteractor(storage, () -> username, presenter);

  @BeforeEach
  void setUp() {
    storage.saveTrip(createTrip(TRIP_ID, 7, username));
  }

  @Test
  void firstDayWithNoExpensesHasFullAllowanceAndZeroProjection() {
    saveExpense(storage, "later", START.plusDays(1), ExpenseCategory.FOOD, 43);

    interactor.execute(day(0));

    final DailyBudgetOutputData summary = result();
    assertAmount("250.00", summary.getAllowance());
    assertAmount("0.00", summary.getCarryOver());
    assertAmount("0.00", summary.getSpent());
    assertAmount("250.00", summary.getRemaining());
    assertAmount("0.00", summary.getProjectedCost());
    assertAmount("1750.00", summary.getTotalBudget());
    assertTrue(summary.getCategoryTotals().isEmpty());
  }

  @Test
  void dayTwoUsesHomeCurrencyAndExcludesLaterExpensesFromTheProjection() {
    saveExpense(storage, "flight", START, ExpenseCategory.FLIGHT, 180);
    saveExpense(storage, "lunch", START.plusDays(1), ExpenseCategory.FOOD, 20);
    saveExpense(storage, "dinner", START.plusDays(1), ExpenseCategory.FOOD, 13);
    saveExpense(storage, "train", START.plusDays(1), ExpenseCategory.TRANSPORT, 10);
    saveExpense(storage, "later", START.plusDays(2), ExpenseCategory.LODGING, 700);

    interactor.execute(day(1));

    final DailyBudgetOutputData summary = result();
    assertEquals(START.plusDays(1), summary.getDate());
    assertEquals("CAD", summary.getCurrency());
    assertAmount("70.00", summary.getCarryOver());
    assertAmount("320.00", summary.getAllowance());
    assertAmount("43.00", summary.getSpent());
    assertAmount("277.00", summary.getRemaining());
    assertAmount("780.50", summary.getProjectedCost());
    assertAmount("0.00", summary.getProjectedOverage());
    assertEquals(2, summary.getCategoryTotals().size());
    assertAmount("33.00", summary.getCategoryTotals().get(ExpenseCategory.FOOD));
    assertAmount("10.00", summary.getCategoryTotals().get(ExpenseCategory.TRANSPORT));
  }

  @Test
  void projectionIncludesElapsedDaysWithoutExpenses() {
    saveExpense(storage, "flight", START, ExpenseCategory.FLIGHT, 300);

    interactor.execute(day(2));

    final DailyBudgetOutputData summary = result();
    assertAmount("450.00", summary.getAllowance());
    assertAmount("0.00", summary.getSpent());
    assertAmount("700.00", summary.getProjectedCost());
  }

  @Test
  void reportsBothDailyAndProjectedTripOverages() {
    saveExpense(storage, "flight", START, ExpenseCategory.FLIGHT, 180);
    saveExpense(storage, "food", START.plusDays(1), ExpenseCategory.FOOD, 43);
    saveExpense(storage, "activities", START.plusDays(2), ExpenseCategory.ACTIVITY, 700);

    interactor.execute(day(2));

    final DailyBudgetOutputData summary = result();
    assertAmount("527.00", summary.getAllowance());
    assertAmount("173.00", summary.getDayOverage());
    assertAmount("2153.67", summary.getProjectedCost());
    assertAmount("403.67", summary.getProjectedOverage());
  }

  @Test
  void overspendingReducesTheNextDaysAllowance() {
    saveExpense(storage, "flight", START, ExpenseCategory.FLIGHT, 300);

    interactor.execute(day(1));

    final DailyBudgetOutputData summary = result();
    assertAmount("-50.00", summary.getCarryOver());
    assertAmount("200.00", summary.getAllowance());
  }

  @Test
  void projectionEqualsActualSpendingForASingleDayTrip() {
    storage.saveTrip(createTrip(TRIP_ID, 1, username));
    saveExpense(storage, "flight", START, ExpenseCategory.FLIGHT, 250);

    interactor.execute(day(0));

    final DailyBudgetOutputData summary = result();
    assertAmount("250.00", summary.getProjectedCost());
    assertAmount("0.00", summary.getDayOverage());
    assertAmount("0.00", summary.getProjectedOverage());
  }

  @Test
  void budgetChangeRecalculatesEarlierDaysAndPersistsForOtherMembers(@TempDir Path directory) {
    final String path = directory.resolve("trips.json").toString();
    final JsonFileTripDataAccessObject fileStorage = new JsonFileTripDataAccessObject(path);
    final Trip trip = createTrip(TRIP_ID, 7, "casey");
    trip.addMember(username);
    trip.setSavingsGoal(username, 500);
    fileStorage.saveTrip(trip);
    saveExpense(fileStorage, "flight", START, ExpenseCategory.FLIGHT, 180);
    saveExpense(fileStorage, "food", START.plusDays(1), ExpenseCategory.FOOD, 43);
    final DailyBudgetInteractor fileInteractor =
        new DailyBudgetInteractor(fileStorage, () -> username, presenter);

    fileInteractor.changeBudget(new ChangeBudgetInputData(day(1), " 300 "));

    final DailyBudgetOutputData summary = result();
    assertTrue(presenter.updated);
    assertAmount("420.00", summary.getAllowance());
    assertAmount("377.00", summary.getRemaining());
    assertAmount("780.50", summary.getProjectedCost());
    assertAmount("2100.00", summary.getTotalBudget());

    final JsonFileTripDataAccessObject reopened = new JsonFileTripDataAccessObject(path);
    assertEquals(300.0, reopened.getTrip(TRIP_ID).getDailyBudget());
    assertEquals(500.0, reopened.getTrip(TRIP_ID).getSavingsGoal(username));
    final DailyBudgetInteractor otherMember =
        new DailyBudgetInteractor(reopened, () -> "casey", presenter);
    otherMember.execute(day(0));
    assertAmount("300.00", result().getAllowance());
    assertFalse(presenter.updated);
  }

  @Test
  void acceptsZeroBudget() {
    interactor.changeBudget(new ChangeBudgetInputData(day(0), "0"));

    assertAmount("0.00", result().getTotalBudget());
    assertEquals(0.0, storage.getTrip(TRIP_ID).getDailyBudget());
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "abc", "-1", "NaN", "Infinity", "1e999"})
  void invalidAmountKeepsTheCurrentBudgetAndSummary(String amount) {
    interactor.execute(day(0));
    final DailyBudgetOutputData previous = result();

    interactor.changeBudget(new ChangeBudgetInputData(day(0), amount));

    assertEquals("Daily budget must be a number (0 or more).", presenter.error);
    assertEquals(previous, presenter.summary);
    assertFalse(presenter.updated);
    assertEquals(250.0, storage.getTrip(TRIP_ID).getDailyBudget());
  }

  @Test
  void rejectsDateOutsideTripBeforeChangingBudget() {
    interactor.changeBudget(new ChangeBudgetInputData(day(7), "100"));

    assertEquals("Choose a date within the trip.", presenter.error);
    assertNull(presenter.summary);
    assertEquals(250.0, storage.getTrip(TRIP_ID).getDailyBudget());
  }

  @Test
  void showsOnlyTripsThatTheSignedInUserBelongsTo() {
    storage.saveTrip(createTrip("other", 7, "casey"));
    storage.setCurrentTripId("other");

    interactor.open();

    assertEquals(1, presenter.trips.size());
    assertEquals(TRIP_ID, presenter.trips.get(0).getId());
  }

  @Test
  void prefersTheCurrentTripAndRemembersTheTripViewed() {
    storage.saveTrip(createTrip("second", 2, username));
    storage.setCurrentTripId("second");

    interactor.open();
    assertEquals("second", presenter.trips.get(0).getId());

    interactor.execute(day(0));
    assertEquals(TRIP_ID, storage.getCurrentTripId());
    interactor.open();
    assertEquals(TRIP_ID, presenter.trips.get(0).getId());
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = {"casey"})
  void preventsNonmembersAndSignedOutUsersFromViewingOrUpdating(String unauthorizedUser) {
    username = unauthorizedUser;
    interactor.execute(day(0));

    assertEquals("Choose a trip you belong to.", presenter.error);
    assertNull(presenter.summary);

    interactor.changeBudget(new ChangeBudgetInputData(day(0), "100"));

    assertNull(presenter.summary);
    assertEquals(250.0, storage.getTrip(TRIP_ID).getDailyBudget());
  }

  private DailyBudgetOutputData result() {
    assertNull(presenter.error);
    assertNotNull(presenter.summary);
    return presenter.summary;
  }

  private static void assertAmount(String expected, BigDecimal actual) {
    assertEquals(new BigDecimal(expected), actual.setScale(2, RoundingMode.HALF_UP));
  }

  private static DailyBudgetInputData day(int offset) {
    return new DailyBudgetInputData(TRIP_ID, START.plusDays(offset));
  }

  private static Trip createTrip(String id, int days, String owner) {
    return new Trip(id, "Tokyo", "Japan", START, START.plusDays(days - 1), "CAD", 250, owner);
  }

  private static void saveExpense(
      InMemoryTripDataAccessObject trips, String id, LocalDate date, ExpenseCategory category,
      double amountInCad) {
    trips.saveExpense(new Expense(id, TRIP_ID, "alex", id, 5000, "JPY", amountInCad, category, date));
  }

  private static final class RecordingPresenter implements DailyBudgetOutputBoundary {
    private DailyBudgetOutputData summary;
    private List<DailyBudgetTripOption> trips;
    private String error;
    private boolean updated;

    @Override
    public void showTrips(List<DailyBudgetTripOption> options) {
      trips = options;
    }

    @Override
    public void showSummary(DailyBudgetOutputData result, boolean budgetUpdated) {
      summary = result;
      updated = budgetUpdated;
    }

    @Override
    public void showError(String message) {
      error = message;
    }
  }
}
