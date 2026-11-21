package view;

import entity.ExpenseCategory;
import interface_adapter.daily_budget.DailyBudgetController;
import interface_adapter.daily_budget.DailyBudgetState;
import interface_adapter.daily_budget.DailyBudgetViewModel;
import interface_adapter.daily_budget.DailyBudgetViewModelListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import use_case.daily_budget.DailyBudgetOutputData;
import use_case.daily_budget.DailyBudgetTripOption;

/** Lets a signed-in member view a day and change the trip's daily budget. */
public final class DailyBudgetView extends JPanel implements DailyBudgetViewModelListener {
  private static final Color ERROR_COLOR = new Color(165, 27, 41);

  private final DailyBudgetViewModel model;
  private DailyBudgetController controller;
  private Runnable backAction = () -> {};
  private List<DailyBudgetTripOption> displayedTrips = List.of();
  private DailyBudgetOutputData displayedSummary;
  private boolean updatingPickers;

  private final JComboBox<DailyBudgetTripOption> trips = new JComboBox<>();
  private final JComboBox<LocalDate> days = new JComboBox<>();
  private final JTextField budget = new JTextField(10);
  private final JButton update = new JButton("Update Budget");
  private final JLabel feedback = new JLabel(" ");
  private final JPanel summary = new JPanel();

  public DailyBudgetView(DailyBudgetViewModel model) {
    super(new BorderLayout(0, 8));
    this.model = model;
    setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

    final JPanel controls = new JPanel();
    controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
    final JButton back = new JButton("Back");
    back.addActionListener(event -> backAction.run());
    controls.add(row(back, emphasized("Daily Budget")));
    controls.add(row(labelFor("Trip", trips), trips));
    controls.add(row(labelFor("Day", days), days));
    controls.add(row(labelFor("Daily budget", budget), budget, update));
    controls.add(row(feedback));
    add(controls, BorderLayout.NORTH);

    summary.setLayout(new BoxLayout(summary, BoxLayout.Y_AXIS));
    summary.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
    final JScrollPane scroll = new JScrollPane(summary);
    scroll.setPreferredSize(new Dimension(470, 270));
    add(scroll, BorderLayout.CENTER);

    trips.addActionListener(event -> {
      if (!updatingPickers) {
        selectTrip();
      }
    });
    days.addActionListener(event -> {
      if (!updatingPickers) {
        requestDay();
      }
    });
    update.addActionListener(event -> {
      final DailyBudgetTripOption trip = selectedTrip();
      if (trip != null) {
        controller.changeBudget(trip.getId(), selectedDay(), budget.getText());
      }
    });

    selectTrip();
    model.addListener(this);
  }

  public void setDailyBudgetController(DailyBudgetController controller) {
    this.controller = controller;
  }

  public void setBackAction(Runnable backAction) {
    this.backAction = backAction;
  }

  @Override
  public void stateChanged() {
    final DailyBudgetState state = model.getState();
    if (!displayedTrips.equals(state.getTrips())) {
      displayedTrips = state.getTrips();
      updatingPickers = true;
      trips.setModel(new DefaultComboBoxModel<>(displayedTrips.toArray(new DailyBudgetTripOption[0])));
      updatingPickers = false;
      selectTrip();
      return;
    }
    if (state.getSummary() != null && state.getSummary() != displayedSummary) {
      displayedSummary = state.getSummary();
      renderSummary(displayedSummary);
    }
    showFeedback(state.getMessage().isEmpty() ? " " : state.getMessage(), state.hasError());
  }

  private void selectTrip() {
    final DailyBudgetTripOption trip = selectedTrip();
    final boolean hasTrip = trip != null;
    days.setEnabled(hasTrip);
    budget.setEnabled(hasTrip);
    update.setEnabled(hasTrip);
    displayedSummary = null;
    summary.removeAll();
    budget.setText("");
    showFeedback(" ", false);

    final DefaultComboBoxModel<LocalDate> dayOptions = new DefaultComboBoxModel<>();
    if (hasTrip) {
      trip.getStart().datesUntil(trip.getEnd()).forEach(dayOptions::addElement);
      dayOptions.addElement(trip.getEnd());
    }
    updatingPickers = true;
    days.setModel(dayOptions);
    updatingPickers = false;
    if (hasTrip) {
      requestDay();
    } else {
      summary.add(new JLabel("You don't have any trips yet."));
      refreshSummary();
    }
  }

  private DailyBudgetTripOption selectedTrip() {
    return (DailyBudgetTripOption) trips.getSelectedItem();
  }

  private LocalDate selectedDay() {
    return (LocalDate) days.getSelectedItem();
  }

  private void requestDay() {
    final DailyBudgetTripOption trip = selectedTrip();
    if (trip != null) {
      controller.showDay(trip.getId(), selectedDay());
    }
  }

  private void renderSummary(DailyBudgetOutputData data) {
    final String currency = data.getCurrency();
    budget.setText(data.getDailyBudget().stripTrailingZeros().toPlainString());
    summary.removeAll();
    summary.add(emphasized("Budget for " + data.getDate()));
    summary.add(new JLabel("Allowed: " + money(data.getAllowance(), currency)));
    summary.add(new JLabel("Daily budget: " + money(data.getDailyBudget(), currency)
        + "   Carry-over: " + money(data.getCarryOver(), currency)));
    summary.add(new JLabel("Spent: " + money(data.getSpent(), currency)));
    if (data.getDayOverage().signum() > 0) {
      addWarning("Over the day's allowance by " + money(data.getDayOverage(), currency));
    } else {
      summary.add(new JLabel("Left: " + money(data.getRemaining(), currency)));
    }

    summary.add(emphasized("Spending by category"));
    if (data.getCategoryTotals().isEmpty()) {
      summary.add(new JLabel("No expenses for this day."));
    }
    for (Map.Entry<ExpenseCategory, BigDecimal> entry : data.getCategoryTotals().entrySet()) {
      summary.add(new JLabel(entry.getKey().getDisplayName() + ": "
          + money(entry.getValue(), currency)));
    }

    summary.add(emphasized("Projected trip cost: " + money(data.getProjectedCost(), currency)
        + " of " + money(data.getTotalBudget(), currency)));
    if (data.getProjectedOverage().signum() > 0) {
      addWarning("Projected to go over the trip budget by "
          + money(data.getProjectedOverage(), currency));
    } else {
      summary.add(new JLabel("Within the trip budget at this pace."));
    }
    refreshSummary();
  }

  private void showFeedback(String message, boolean error) {
    feedback.setForeground(error ? ERROR_COLOR : getForeground());
    feedback.setText(message);
  }

  private void addWarning(String message) {
    final JLabel label = emphasized(message);
    label.setForeground(ERROR_COLOR);
    summary.add(label);
  }

  private void refreshSummary() {
    summary.revalidate();
    summary.repaint();
  }

  private static JLabel emphasized(String text) {
    final JLabel label = new JLabel(text);
    label.setFont(label.getFont().deriveFont(Font.BOLD));
    return label;
  }

  private static JLabel labelFor(String text, Component component) {
    final JLabel label = new JLabel(text);
    label.setLabelFor(component);
    return label;
  }

  private static JPanel row(Component... components) {
    final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING));
    for (Component component : components) {
      panel.add(component);
    }
    return panel;
  }

  private static String money(BigDecimal amount, String currency) {
    return amount.setScale(2, RoundingMode.HALF_UP).toPlainString() + " " + currency;
  }
}
