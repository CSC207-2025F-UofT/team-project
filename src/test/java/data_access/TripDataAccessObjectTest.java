package data_access;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import entity.ActivityIdea;
import entity.Contribution;
import entity.Expense;
import entity.ExpenseCategory;
import entity.Trip;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class TripDataAccessObjectTest {

  private static final LocalDate START = LocalDate.of(2027, 2, 13);

  private static Trip sampleTrip(String id, String owner) {
    final Trip trip = new Trip(id, "Tokyo", "Japan", START, START.plusDays(4), "CAD", 250, owner);
    trip.addMember("amelia");
    trip.setSavingsGoal("amelia", 1200);
    return trip;
  }

  private static void fill(InMemoryTripDataAccessObject dao) {
    dao.saveTrip(sampleTrip("t1", "tamima"));
    dao.saveTrip(new Trip("t2", "Montreal", "Canada", START, START, "CAD", 100, "jadyn"));
    dao.saveExpense(
        new Expense("e1", "t1", "jadyn", "Ramen", 4800, "JPY", 43, ExpenseCategory.FOOD, START));
    dao.saveExpense(
        new Expense("e2", "t2", "jadyn", "Bagel", 5, "CAD", 5, ExpenseCategory.FOOD, START));
    dao.saveContribution(new Contribution("c1", "t1", "amelia", 300, START.minusMonths(1)));
    final ActivityIdea idea =
        new ActivityIdea(
            "a1", "t1", START, "teamLab", 40, "emily", LocalDateTime.of(2026, 11, 16, 12, 0));
    idea.addVote("emily");
    dao.saveActivityIdea(idea);
  }

  @Test
  void inMemoryStoresAndFiltersByTrip() {
    final InMemoryTripDataAccessObject dao = new InMemoryTripDataAccessObject();
    fill(dao);

    assertTrue(dao.existsTrip("t1"));
    assertFalse(dao.existsTrip("missing"));
    assertNull(dao.getTrip("missing"));
    assertEquals(1, dao.getTripsForMember("amelia").size());
    assertEquals(1, dao.getTripsForMember("jadyn").size());
    assertEquals(0, dao.getTripsForMember("nobody").size());
    assertEquals(1, dao.getExpenses("t1").size());
    assertEquals(1, dao.getContributions("t1").size());
    assertEquals(0, dao.getContributions("t2").size());
    assertEquals(1, dao.getActivityIdeas("t1").size());
    assertEquals("teamLab", dao.getActivityIdea("a1").getTitle());
  }

  @Test
  void savingWithSameIdReplaces() {
    final InMemoryTripDataAccessObject dao = new InMemoryTripDataAccessObject();
    fill(dao);
    final Trip updated = dao.getTrip("t1");
    updated.addMember("emily");
    dao.saveTrip(updated);
    assertEquals(1, dao.getTripsForMember("emily").size());
    assertEquals(1, dao.getTripsForMember("tamima").size());
  }

  @Test
  void currentTripIdIsRemembered() {
    final InMemoryTripDataAccessObject dao = new InMemoryTripDataAccessObject();
    assertNull(dao.getCurrentTripId());
    dao.setCurrentTripId("t1");
    assertEquals("t1", dao.getCurrentTripId());
  }

  @Test
  void jsonFileSurvivesRestart(@TempDir Path dir) {
    final String path = dir.resolve("trips.json").toString();
    fill(new JsonFileTripDataAccessObject(path));

    final JsonFileTripDataAccessObject reloaded = new JsonFileTripDataAccessObject(path);
    final Trip trip = reloaded.getTrip("t1");
    assertEquals("Tokyo", trip.getName());
    assertEquals(START.plusDays(4), trip.getEndDate());
    assertEquals(List.of("tamima", "amelia"), trip.getMemberUsernames());
    assertEquals(1200, trip.getSavingsGoal("amelia"));

    final Expense expense = reloaded.getExpenses("t1").get(0);
    assertEquals("JPY", expense.getCurrency());
    assertEquals(43, expense.getAmountInHomeCurrency());
    assertEquals(ExpenseCategory.FOOD, expense.getCategory());

    assertEquals(300, reloaded.getContributions("t1").get(0).getAmount());

    final ActivityIdea idea = reloaded.getActivityIdea("a1");
    assertEquals(Set.of("emily"), idea.getVoterUsernames());
    assertEquals(LocalDateTime.of(2026, 11, 16, 12, 0), idea.getCreatedAt());
  }

  @Test
  void newFileIsCreatedAndEmptyFileIsAccepted(@TempDir Path dir) throws IOException {
    final Path created = dir.resolve("new.json");
    new JsonFileTripDataAccessObject(created.toString());
    assertTrue(Files.exists(created));

    final Path empty = dir.resolve("empty.json");
    Files.writeString(empty, "");
    assertTrue(new JsonFileTripDataAccessObject(empty.toString()).getTripsForMember("x").isEmpty());
  }

  @Test
  void corruptFileGivesClearError(@TempDir Path dir) throws IOException {
    final Path bad = dir.resolve("bad.json");
    Files.writeString(bad, "{not json");
    assertThrows(DataAccessException.class, () -> new JsonFileTripDataAccessObject(bad.toString()));
  }
}
