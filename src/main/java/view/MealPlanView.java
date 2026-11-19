package view;

import interface_adapter.load_meal_plan.LoadMealPlanController;
import interface_adapter.meal_plan.MealPlanSlotViewData;
import interface_adapter.meal_plan.MealPlanState;
import interface_adapter.meal_plan.MealPlanViewModel;
import interface_adapter.move_planned_meal.MovePlannedMealController;
import interface_adapter.remove_planned_meal.RemovePlannedMealController;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * The meal plan page (use case 3): a 7 x 3 grid of the current week and a side
 * panel for the selected meal, with Remove and Move.
 */
public class MealPlanView extends JPanel implements MealPlanViewModel.MealPlanViewModelListener {

    private static final String[] MEAL_TYPES = {"BREAKFAST", "LUNCH", "DINNER"};
    private static final Color SELECTED_BORDER = new Color(0x2F6FDE);
    private static final Color MOVE_TARGET = new Color(0xE3F4E1);
    private static final Color COOKED = new Color(0xEDEDED);
    private static final DateTimeFormatter WEEK_FORMAT = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);

    private final MealPlanViewModel viewModel;
    private final LoadMealPlanController loadController;
    private final RemovePlannedMealController removeController;
    private final MovePlannedMealController moveController;

    private final JLabel weekLabel = new JLabel(" ");
    private final JPanel grid = new JPanel(new GridLayout(3, 8, 4, 4));
    private final List<JButton> cellButtons = new ArrayList<>();
    private final JLabel selectedTitle = new JLabel("Select a meal");
    private final JLabel selectedStatus = new JLabel(" ");
    private final JTextArea ingredientsArea = new JTextArea();
    private final JButton removeButton = new JButton("Remove");
    private final JButton moveButton = new JButton("Move");
    private final JButton cancelMoveButton = new JButton("Cancel move");
    private final JLabel messageLabel = new JLabel(" ");
    private final JLabel errorLabel = new JLabel(" ");

    private DayOfWeek selectedDay;
    private String selectedMealType;
    private MealPlanSlotViewData moveSource;
    private MealPlanSlotViewData pendingMoveTarget;

    /**
     * Creates the meal plan page.
     *
     * @param viewModel the meal plan view model
     * @param loadController loads the week (3a)
     * @param removeController removes a meal (3b)
     * @param moveController moves a meal (3b)
     */
    public MealPlanView(MealPlanViewModel viewModel,
                        LoadMealPlanController loadController,
                        RemovePlannedMealController removeController,
                        MovePlannedMealController moveController) {
        this.viewModel = viewModel;
        this.loadController = loadController;
        this.removeController = removeController;
        this.moveController = moveController;

        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel title = new JLabel("Meal Plan");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        JPanel top = new JPanel(new BorderLayout());
        top.add(title, BorderLayout.WEST);
        top.add(weekLabel, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        JPanel dayHeaders = new JPanel(new GridLayout(1, 8, 4, 4));
        dayHeaders.add(new JLabel(""));
        for (DayOfWeek day : DayOfWeek.values()) {
            dayHeaders.add(centeredBold(day.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)));
        }
        JPanel week = new JPanel(new BorderLayout(4, 4));
        week.add(dayHeaders, BorderLayout.NORTH);
        week.add(grid, BorderLayout.CENTER);
        add(week, BorderLayout.CENTER);
        add(buildSidePanel(), BorderLayout.EAST);

        errorLabel.setForeground(Color.RED.darker());
        JPanel bottom = new JPanel(new GridLayout(2, 1));
        bottom.add(messageLabel);
        bottom.add(errorLabel);
        add(bottom, BorderLayout.SOUTH);

        removeButton.addActionListener(event -> removeSelected());
        moveButton.addActionListener(event -> startMove());
        cancelMoveButton.addActionListener(event -> cancelMove());

        viewModel.addListener(this);
        refreshFromState(viewModel.getState());
    }

    /**
     * Reloads the current week. Call this every time the page becomes visible.
     */
    public void onShown() {
        moveSource = null;
        pendingMoveTarget = null;
        loadController.execute(LocalDate.now());
    }

    @Override
    public void stateChanged() {
        MealPlanState state = viewModel.getState();
        if (pendingMoveTarget != null && state.getError().isEmpty()) {
            selectedDay = pendingMoveTarget.getDay();
            selectedMealType = pendingMoveTarget.getMealType();
        }
        pendingMoveTarget = null;
        refreshFromState(state);
    }

    private JPanel buildSidePanel() {
        selectedTitle.setFont(selectedTitle.getFont().deriveFont(Font.BOLD, 14f));
        selectedStatus.setForeground(Color.DARK_GRAY);
        ingredientsArea.setEditable(false);
        ingredientsArea.setLineWrap(true);
        ingredientsArea.setWrapStyleWord(true);

        JPanel header = new JPanel(new GridLayout(2, 1));
        header.add(selectedTitle);
        header.add(selectedStatus);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        buttons.add(removeButton);
        buttons.add(moveButton);
        buttons.add(cancelMoveButton);

        JPanel side = new JPanel(new BorderLayout(4, 4));
        side.setPreferredSize(new Dimension(240, 0));
        side.setBorder(BorderFactory.createTitledBorder("Selected meal"));
        side.add(header, BorderLayout.NORTH);
        side.add(new JScrollPane(ingredientsArea), BorderLayout.CENTER);
        side.add(buttons, BorderLayout.SOUTH);
        return side;
    }

    private void refreshFromState(MealPlanState state) {
        LocalDate weekStart = state.getWeekStart();
        weekLabel.setText(weekStart == null ? " " : "Week of " + weekStart.format(WEEK_FORMAT));
        rebuildGrid(state.getSlots());
        refreshSidePanel(findSlot(state.getSlots(), selectedDay, selectedMealType));
        if (moveSource != null) {
            messageLabel.setText("Click an empty slot to move " + moveSource.getRecipeName() + " there.");
        }
        else {
            messageLabel.setText(blankToSpace(state.getMessage()));
        }
        errorLabel.setText(blankToSpace(state.getError()));
        revalidate();
        repaint();
    }

    private void rebuildGrid(List<MealPlanSlotViewData> slots) {
        grid.removeAll();
        cellButtons.clear();
        for (String mealType : MEAL_TYPES) {
            grid.add(centeredBold(capitalize(mealType)));
            for (DayOfWeek day : DayOfWeek.values()) {
                MealPlanSlotViewData slot = findSlot(slots, day, mealType);
                if (slot == null) {
                    slot = new MealPlanSlotViewData(day, mealType, "", false, List.of());
                }
                grid.add(createCell(slot));
            }
        }
    }

    private JButton createCell(MealPlanSlotViewData slot) {
        JButton cell = new JButton(cellText(slot));
        cell.setFocusPainted(false);
        cell.setToolTipText(capitalize(slot.getMealType()) + ", "
                + slot.getDay().getDisplayName(TextStyle.FULL, Locale.ENGLISH));
        if (moveSource != null && !slot.isFilled()) {
            cell.setBackground(MOVE_TARGET);
        }
        else if (slot.isCooked()) {
            cell.setBackground(COOKED);
        }
        if (slot.getDay() == selectedDay && slot.getMealType().equals(selectedMealType)) {
            cell.setBorder(BorderFactory.createLineBorder(SELECTED_BORDER, 2));
        }
        cell.addActionListener(event -> cellClicked(slot));
        cellButtons.add(cell);
        return cell;
    }

    private void cellClicked(MealPlanSlotViewData slot) {
        LocalDate weekStart = viewModel.getState().getWeekStart();
        if (moveSource != null && weekStart != null) {
            MealPlanSlotViewData source = moveSource;
            moveSource = null;
            pendingMoveTarget = slot;
            moveController.execute(weekStart, source.getDay(), source.getMealType(),
                    slot.getDay(), slot.getMealType());
            return;
        }
        selectedDay = slot.getDay();
        selectedMealType = slot.getMealType();
        refreshFromState(viewModel.getState());
    }

    private void removeSelected() {
        LocalDate weekStart = viewModel.getState().getWeekStart();
        if (weekStart != null && selectedDay != null) {
            moveSource = null;
            removeController.execute(weekStart, selectedDay, selectedMealType);
        }
    }

    private void startMove() {
        MealPlanSlotViewData selected =
                findSlot(viewModel.getState().getSlots(), selectedDay, selectedMealType);
        if (selected != null && selected.isFilled()) {
            moveSource = selected;
            viewModel.getState().setError("");
            refreshFromState(viewModel.getState());
        }
    }

    private void cancelMove() {
        moveSource = null;
        refreshFromState(viewModel.getState());
    }

    private void refreshSidePanel(MealPlanSlotViewData selected) {
        boolean filled = selected != null && selected.isFilled();
        if (selected == null) {
            selectedTitle.setText("Select a meal");
            selectedStatus.setText(" ");
        }
        else if (!filled) {
            selectedTitle.setText("Empty slot");
            selectedStatus.setText(capitalize(selected.getMealType()) + ", "
                    + selected.getDay().getDisplayName(TextStyle.FULL, Locale.ENGLISH));
        }
        else {
            selectedTitle.setText(selected.getRecipeName());
            selectedStatus.setText(selected.isCooked() ? "Cooked" : "Not cooked yet");
        }
        ingredientsArea.setText(filled ? String.join("\n", selected.getIngredientLines()) : "");
        boolean moving = moveSource != null;
        removeButton.setEnabled(filled);
        moveButton.setEnabled(filled);
        removeButton.setVisible(!moving);
        moveButton.setVisible(!moving);
        cancelMoveButton.setVisible(moving);
    }

    private static MealPlanSlotViewData findSlot(List<MealPlanSlotViewData> slots,
                                                 DayOfWeek day, String mealType) {
        for (MealPlanSlotViewData slot : slots) {
            if (slot.getDay() == day && slot.getMealType().equals(mealType)) {
                return slot;
            }
        }
        return null;
    }

    private static String cellText(MealPlanSlotViewData slot) {
        if (!slot.isFilled()) {
            return "-";
        }
        String name = escapeHtml(slot.getRecipeName());
        String cooked = slot.isCooked() ? "<br><small>cooked</small>" : "";
        return "<html><center>" + name + cooked + "</center></html>";
    }

    private static JLabel centeredBold(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        return label;
    }

    private static String capitalize(String upperCase) {
        return upperCase.charAt(0) + upperCase.substring(1).toLowerCase(Locale.ENGLISH);
    }

    private static String escapeHtml(String text) {
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    private static String blankToSpace(String text) {
        if (text == null || text.isBlank()) {
            return " ";
        }
        return text;
    }

    /**
     * Returns the 21 meal cell buttons row by row: breakfasts Monday to Sunday,
     * then lunches, then dinners. Used by tests.
     *
     * @return the cell buttons
     */
    List<JButton> getCellButtons() {
        return cellButtons;
    }

    JButton getRemoveButton() {
        return removeButton;
    }

    JButton getMoveButton() {
        return moveButton;
    }

    JButton getCancelMoveButton() {
        return cancelMoveButton;
    }

    JLabel getSelectedTitle() {
        return selectedTitle;
    }

    JTextArea getIngredientsArea() {
        return ingredientsArea;
    }

    JLabel getMessageLabel() {
        return messageLabel;
    }

    JLabel getErrorLabel() {
        return errorLabel;
    }
}
