package app;

import data_access.FileUserDataAccessObject;
import data_access.JsonFileTripDataAccessObject;
import entity.ActivityIdea;
import entity.Contribution;
import entity.Expense;
import entity.ExpenseCategory;
import entity.Trip;
import entity.UserFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Fills users.csv and trips.json with a sample trip so everyone can build and test their own screen
 * without waiting for the Create Trip use case to be finished. Also handy for preparing the demo
 * before the final presentation.
 *
 * <p>Run this main method once. Then log in as any of the sample users (password "password") and
 * the trip "Tokyo Reading Week" will exist. Delete users.csv and trips.json to start fresh.
 */
public final class SampleDataSeeder {

  /** The id of the sample trip, so you can hard-code it while developing. */
  public static final String SAMPLE_TRIP_ID = "sample-tokyo-trip";

  private static final String[] MEMBERS = {"tamima", "amelia", "jadyn", "jessica", "emily"};
  private static final String PASSWORD = "password";
  private static final LocalDate START = LocalDate.of(2027, 2, 13);
  private static final double DAILY_BUDGET = 250.0;
  private static final double SAVINGS_GOAL = 2000.0;
  private static final double FIRST_DEPOSIT = 400.0;
  private static final double FLIGHT_CAD = 1250.0;
  private static final double RAMEN_JPY = 4800.0;
  private static final double RAMEN_CAD = 43.0;
  private static final double TEAMLAB_CAD = 160.0;
  private static final int TRIP_DAYS = 7;
  private static final int PROPOSAL_HOUR = 10;

  private SampleDataSeeder() {}

  /**
   * Writes the sample data.
   *
   * @param args unused
   */
  public static void main(String[] args) {
    final FileUserDataAccessObject users =
        new FileUserDataAccessObject("users.csv", new UserFactory());
    for (String member : MEMBERS) {
      if (!users.existsByName(member)) {
        users.save(new UserFactory().create(member, PASSWORD));
      }
    }

    final JsonFileTripDataAccessObject data = new JsonFileTripDataAccessObject("trips.json");
    final Trip trip =
        new Trip(
            SAMPLE_TRIP_ID,
            "Tokyo Reading Week",
            "Tokyo, Japan",
            START,
            START.plusDays(TRIP_DAYS - 1),
            "CAD",
            DAILY_BUDGET,
            MEMBERS[0]);
    for (String member : MEMBERS) {
      trip.addMember(member);
      trip.setSavingsGoal(member, SAVINGS_GOAL);
    }
    data.saveTrip(trip);

    data.saveContribution(
        new Contribution(
            "sample-contribution-1",
            SAMPLE_TRIP_ID,
            "amelia",
            FIRST_DEPOSIT,
            LocalDate.of(2026, 11, 1)));
    data.saveExpense(
        new Expense(
            "sample-expense-1",
            SAMPLE_TRIP_ID,
            "jadyn",
            "Flight to Haneda",
            FLIGHT_CAD,
            "CAD",
            FLIGHT_CAD,
            ExpenseCategory.FLIGHT,
            START));
    data.saveExpense(
        new Expense(
            "sample-expense-2",
            SAMPLE_TRIP_ID,
            "jessica",
            "Ramen in Shibuya",
            RAMEN_JPY,
            "JPY",
            RAMEN_CAD,
            ExpenseCategory.FOOD,
            START.plusDays(1)));

    final ActivityIdea teamLab =
        new ActivityIdea(
            "sample-activity-1",
            SAMPLE_TRIP_ID,
            START.plusDays(1),
            "teamLab Planets",
            TEAMLAB_CAD,
            "emily",
            LocalDateTime.of(2026, 11, 10, PROPOSAL_HOUR, 0));
    teamLab.addVote("emily");
    teamLab.addVote("tamima");
    data.saveActivityIdea(teamLab);

    System.out.println(
        "Sample data written. Log in as any of tamima/amelia/jadyn/jessica/emily "
            + "with password \"password\".");
  }
}
