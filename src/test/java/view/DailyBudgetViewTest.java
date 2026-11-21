package view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import data_access.InMemoryTripDataAccessObject;
import entity.Expense;
import entity.ExpenseCategory;
import entity.Trip;
import interface_adapter.daily_budget.DailyBudgetController;
import interface_adapter.daily_budget.DailyBudgetPresenter;
import interface_adapter.daily_budget.DailyBudgetViewModel;
import java.awt.Component;
import java.awt.Container;
import java.time.LocalDate;
import java.util.stream.Stream;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import org.junit.jupiter.api.Test;
import use_case.daily_budget.DailyBudgetInteractor;

class DailyBudgetViewTest {
  @Test
  void selectingADayShowsBothOveragesAndInvalidUpdateKeepsTheSummary() throws Exception {
    // This test exercises Swing components without opening a window.
    System.setProperty("java.awt.headless", "true");
    SwingUtilities.invokeAndWait(() -> {
      final LocalDate start = LocalDate.of(2027, 2, 13);
      final InMemoryTripDataAccessObject storage = new InMemoryTripDataAccessObject();
      storage.saveTrip(new Trip("tokyo", "Tokyo", "Japan", start, start.plusDays(2),
          "CAD", 100, "alex"));
      storage.saveExpense(new Expense("first", "tokyo", "alex", "Lunch", 60, "CAD", 60,
          ExpenseCategory.FOOD, start));
      storage.saveExpense(new Expense("second", "tokyo", "alex", "Hotel", 180, "CAD", 180,
          ExpenseCategory.LODGING, start.plusDays(1)));
      final DailyBudgetViewModel model = new DailyBudgetViewModel();
      final DailyBudgetController controller = new DailyBudgetController(
          new DailyBudgetInteractor(storage, () -> "alex", new DailyBudgetPresenter(model)));
      final DailyBudgetView view = new DailyBudgetView(model);
      view.setDailyBudgetController(controller);

      controller.open();
      final JComboBox<?> dayPicker = (JComboBox<?>) label(view, "Day").getLabelFor();
      assertEquals(3, dayPicker.getItemCount());
      dayPicker.setSelectedItem(start.plusDays(1));

      final JLabel dailyWarning = label(view, "Over the day's allowance by 40.00 CAD");
      final JLabel projectionWarning = label(view, "Projected to go over the trip budget by 60.00 CAD");
      assertTrue(dailyWarning.getFont().isBold());
      assertTrue(projectionWarning.getFont().isBold());
      assertNotEquals(label(view, "Spent: 180.00 CAD").getForeground(), dailyWarning.getForeground());

      final JTextField budgetField = (JTextField) label(view, "Daily budget").getLabelFor();
      final JButton updateButton = components(view)
          .filter(JButton.class::isInstance)
          .map(JButton.class::cast)
          .filter(button -> button.getText().equals("Update Budget"))
          .findFirst().orElseThrow();
      budgetField.setText("abc");
      updateButton.doClick(0);

      label(view, "Daily budget must be a number (0 or more).");
      label(view, "Projected trip cost: 360.00 CAD of 300.00 CAD");
      assertEquals("abc", budgetField.getText());
      assertEquals(100.0, storage.getTrip("tokyo").getDailyBudget());

      budgetField.setText("150");
      updateButton.doClick(0);
      label(view, "Daily budget updated.");
      label(view, "Allowed: 240.00 CAD");
      label(view, "Projected trip cost: 360.00 CAD of 450.00 CAD");
      assertEquals(150.0, storage.getTrip("tokyo").getDailyBudget());
    });
  }

  private static JLabel label(Container root, String text) {
    return components(root)
        .filter(JLabel.class::isInstance)
        .map(JLabel.class::cast)
        .filter(label -> text.equals(label.getText()))
        .findFirst().orElseThrow(() -> new AssertionError("Missing label: " + text));
  }

  private static Stream<Component> components(Container root) {
    return Stream.of(root.getComponents()).flatMap(component -> {
      if (component instanceof Container) {
        return Stream.concat(Stream.of(component), components((Container) component));
      }
      return Stream.of(component);
    });
  }
}
