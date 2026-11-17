package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class ExpenseContributionTest {

  private static final LocalDate DAY = LocalDate.of(2027, 2, 14);

  @Test
  void expenseStoresFields() {
    final Expense expense =
        new Expense("e1", "t1", "jadyn", "Ramen", 4800, "jpy", 43, ExpenseCategory.FOOD, DAY);
    assertEquals("e1", expense.getId());
    assertEquals("t1", expense.getTripId());
    assertEquals("jadyn", expense.getPayerUsername());
    assertEquals("Ramen", expense.getDescription());
    assertEquals(4800, expense.getAmount());
    assertEquals("JPY", expense.getCurrency());
    assertEquals(43, expense.getAmountInHomeCurrency());
    assertEquals(ExpenseCategory.FOOD, expense.getCategory());
    assertEquals(DAY, expense.getDate());
  }

  @Test
  void expenseRejectsInvalidValues() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new Expense("e1", "t1", "jadyn", "Ramen", 0, "JPY", 1, ExpenseCategory.FOOD, DAY));
    assertThrows(
        IllegalArgumentException.class,
        () -> new Expense("e1", "t1", "jadyn", " ", 10, "JPY", 1, ExpenseCategory.FOOD, DAY));
    assertThrows(
        IllegalArgumentException.class,
        () -> new Expense("e1", "t1", "jadyn", "Ramen", 10, "JPY", 1, null, DAY));
    assertThrows(
        IllegalArgumentException.class,
        () -> new Expense("e1", "t1", "jadyn", "Ramen", 10, null, 1, ExpenseCategory.FOOD, DAY));
  }

  @Test
  void categoryHasDisplayName() {
    assertEquals("Food", ExpenseCategory.FOOD.getDisplayName());
    assertEquals(ExpenseCategory.OTHER, ExpenseCategory.valueOf("OTHER"));
  }

  @Test
  void contributionStoresFields() {
    final Contribution contribution = new Contribution("c1", "t1", "amelia", 250, DAY);
    assertEquals("c1", contribution.getId());
    assertEquals("t1", contribution.getTripId());
    assertEquals("amelia", contribution.getMemberUsername());
    assertEquals(250, contribution.getAmount());
    assertEquals(DAY, contribution.getDate());
  }

  @Test
  void contributionRejectsInvalidValues() {
    assertThrows(
        IllegalArgumentException.class, () -> new Contribution("c1", "t1", "amelia", -5, DAY));
    assertThrows(
        IllegalArgumentException.class,
        () -> new Contribution("c1", "t1", "amelia", Double.POSITIVE_INFINITY, DAY));
    assertThrows(
        IllegalArgumentException.class, () -> new Contribution("c1", "t1", "amelia", 5, null));
    assertThrows(
        IllegalArgumentException.class, () -> new Contribution("c1", null, "amelia", 5, DAY));
  }
}
