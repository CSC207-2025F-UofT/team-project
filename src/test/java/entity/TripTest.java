package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TripTest {

  private static final LocalDate START = LocalDate.of(2027, 2, 13);
  private static final LocalDate END = LocalDate.of(2027, 2, 15);
  private Trip trip;

  @BeforeEach
  void setUp() {
    trip = new Trip("t1", " Tokyo ", "Japan", START, END, "cad", 200, "tamima");
  }

  @Test
  void constructorStoresFieldsAndNormalizes() {
    assertEquals("t1", trip.getId());
    assertEquals("Tokyo", trip.getName());
    assertEquals("Japan", trip.getDestination());
    assertEquals(START, trip.getStartDate());
    assertEquals(END, trip.getEndDate());
    assertEquals("CAD", trip.getHomeCurrency());
    assertEquals(200, trip.getDailyBudget());
    assertEquals("tamima", trip.getOwnerUsername());
    assertEquals(List.of("tamima"), trip.getMemberUsernames());
  }

  @Test
  void endBeforeStartIsRejected() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new Trip("t1", "Tokyo", "Japan", END, START, "CAD", 200, "tamima"));
  }

  @Test
  void sameDayTripIsAllowed() {
    final Trip dayTrip = new Trip("t2", "Niagara", "Ontario", START, START, "CAD", 50, "amelia");
    assertEquals(1, dayTrip.getNumberOfDays());
  }

  @Test
  void invalidFieldsAreRejected() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new Trip("t1", "", "Japan", START, END, "CAD", 200, "tamima"));
    assertThrows(
        IllegalArgumentException.class,
        () -> new Trip("t1", "Tokyo", "Japan", null, END, "CAD", 200, "tamima"));
    assertThrows(
        IllegalArgumentException.class,
        () -> new Trip("t1", "Tokyo", "Japan", START, END, "NOPE", 200, "tamima"));
    assertThrows(
        IllegalArgumentException.class,
        () -> new Trip("t1", "Tokyo", "Japan", START, END, "CAD", -1, "tamima"));
  }

  @Test
  void addMemberAddsOnce() {
    assertTrue(trip.addMember("amelia"));
    assertFalse(trip.addMember("amelia"));
    assertTrue(trip.hasMember("amelia"));
    assertFalse(trip.hasMember("jadyn"));
    assertEquals(List.of("tamima", "amelia"), trip.getMemberUsernames());
  }

  @Test
  void memberListCannotBeModifiedFromOutside() {
    assertThrows(UnsupportedOperationException.class, () -> trip.getMemberUsernames().add("x"));
  }

  @Test
  void containsDateIsInclusive() {
    assertTrue(trip.containsDate(START));
    assertTrue(trip.containsDate(END));
    assertFalse(trip.containsDate(START.minusDays(1)));
    assertFalse(trip.containsDate(END.plusDays(1)));
    assertFalse(trip.containsDate(null));
  }

  @Test
  void datesAndBudgetsCoverWholeTrip() {
    assertEquals(3, trip.getNumberOfDays());
    assertEquals(List.of(START, START.plusDays(1), END), trip.getDates());
    assertEquals(600, trip.getTotalBudget());
    trip.setDailyBudget(100);
    assertEquals(100, trip.getDailyBudget());
    assertThrows(IllegalArgumentException.class, () -> trip.setDailyBudget(-5));
  }

  @Test
  void savingsGoalsAreTrackedPerMember() {
    trip.addMember("amelia");
    assertFalse(trip.hasSavingsGoal("tamima"));
    assertEquals(0, trip.getSavingsGoal("tamima"));

    trip.setSavingsGoal("tamima", 1500);
    trip.setSavingsGoal("amelia", 1000);

    assertTrue(trip.hasSavingsGoal("tamima"));
    assertEquals(1500, trip.getSavingsGoal("tamima"));
    assertEquals(2500, trip.getTotalSavingsGoal());
    assertEquals(2, trip.getSavingsGoals().size());
  }

  @Test
  void savingsGoalRequiresMemberAndPositiveAmount() {
    assertThrows(IllegalArgumentException.class, () -> trip.setSavingsGoal("stranger", 100));
    assertThrows(IllegalArgumentException.class, () -> trip.setSavingsGoal("tamima", 0));
    assertThrows(IllegalArgumentException.class, () -> trip.setSavingsGoal("tamima", Double.NaN));
  }
}
