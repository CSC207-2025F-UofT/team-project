package view;

import entity.ExpenseCategory;
import interface_adapter.ViewManagerModel;
import interface_adapter.add_expense.AddExpenseState;
import interface_adapter.add_expense.AddExpenseState.ExpenseRow;
import interface_adapter.add_expense.AddExpenseViewModel;
import interface_adapter.add_expense.AddExpenseViewModelListener;
import interface_adapter.add_expense.LogExpenseController;
import interface_adapter.logged_in.LoggedInViewModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 * The View for Use Case 3: Log an expense.
 */
public class AddExpenseView extends JPanel implements AddExpenseViewModelListener {

    private static final Color SUCCESS_COLOR = new Color(27, 127, 55);
    private static final Color ERROR_COLOR = new Color(198, 40, 40);
    private static final int PADDING = 16;
    private static final int GRID_GAP = 10;

    private final AddExpenseViewModel addExpenseViewModel;
    private LogExpenseController logExpenseController;
    private ViewManagerModel viewManagerModel;

    private final JLabel headerLabel = new JLabel("Add Expense");
    private final JTextField descriptionField = new JTextField(16);
    private final JTextField amountField = new JTextField(10);
    private final JTextField currencyField = new JTextField(6);
    private final JComboBox<ExpenseCategory> categoryBox =
            new JComboBox<>(ExpenseCategory.values());
    private final JTextField dateField = new JTextField(10);
    private final JTextField paidByField = new JTextField(10);

    private final JButton saveButton = new JButton("Save");
    private final JButton backButton = new JButton("Back");
    private final JLabel statusLabel = new JLabel(" ");

    private final DefaultTableModel tableModel;
    private final JTable expensesTable;

    /**
     * Creates the {@link AddExpenseView} and registers it as a listener on {@code addExpenseViewModel}.
     *
     * @param addExpenseViewModel the view model observed by this panel
     */
    public AddExpenseView(AddExpenseViewModel addExpenseViewModel) {
        this.addExpenseViewModel = addExpenseViewModel;
        this.addExpenseViewModel.addListener(this);

        setLayout(new BorderLayout(0, 12));
        setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));

        final JPanel topBar = new JPanel(new BorderLayout());
        headerLabel.setFont(headerLabel.getFont().deriveFont(Font.BOLD, 15f));
        topBar.add(headerLabel, BorderLayout.WEST);
        topBar.add(backButton, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        final JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        final JLabel sectionTitle = new JLabel("Log an expense");
        sectionTitle.setFont(sectionTitle.getFont().deriveFont(Font.BOLD, 16f));
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(sectionTitle);
        content.add(Box.createVerticalStrut(10));

        categoryBox.setRenderer(
                new DefaultListCellRenderer() {
                    @Override
                    public Component getListCellRendererComponent(
                            JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                        if (value instanceof ExpenseCategory) {
                            setText(((ExpenseCategory) value).getDisplayName());
                        }
                        return this;
                    }
                });

        final JPanel formGrid = new JPanel(new GridLayout(2, 3, GRID_GAP, GRID_GAP));
        formGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        formGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        formGrid.add(labeledField("Description", descriptionField));
        formGrid.add(labeledField("Amount", amountField));
        formGrid.add(labeledField("Currency", currencyField));
        formGrid.add(labeledField("Category", categoryBox));
        formGrid.add(labeledField("Date (YYYY-MM-DD)", dateField));
        formGrid.add(labeledField("Paid by", paidByField));
        content.add(formGrid);
        content.add(Box.createVerticalStrut(12));

        final JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        actionRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        actionRow.add(saveButton);
        actionRow.add(Box.createHorizontalStrut(12));
        actionRow.add(statusLabel);
        content.add(actionRow);
        content.add(Box.createVerticalStrut(14));

        final JSeparator separator = new JSeparator();
        separator.setAlignmentX(Component.LEFT_ALIGNMENT);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        content.add(separator);
        content.add(Box.createVerticalStrut(10));

        final JLabel expensesTitle = new JLabel("Expenses");
        expensesTitle.setFont(expensesTitle.getFont().deriveFont(Font.BOLD, 14f));
        expensesTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(expensesTitle);
        content.add(Box.createVerticalStrut(6));

        tableModel =
                new DefaultTableModel(new String[] {"Description", "Paid", "In CAD"}, 0) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
        expensesTable = new JTable(tableModel);
        expensesTable.setFillsViewportHeight(true);
        final JScrollPane scrollPane = new JScrollPane(expensesTable);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setPreferredSize(new Dimension(540, 180));
        content.add(scrollPane);

        add(content, BorderLayout.CENTER);

        saveButton.addActionListener(
                evt -> {
                    if (logExpenseController != null) {
                        final AddExpenseState state = addExpenseViewModel.getState();
                        logExpenseController.execute(
                                state.getTripId(),
                                descriptionField.getText(),
                                amountField.getText(),
                                currencyField.getText(),
                                (ExpenseCategory) categoryBox.getSelectedItem(),
                                dateField.getText(),
                                paidByField.getText());
                    }
                });

        backButton.addActionListener(
                evt -> {
                    if (viewManagerModel != null) {
                        viewManagerModel.setState(LoggedInViewModel.VIEW_NAME);
                        viewManagerModel.notifyViewChanged();
                    }
                });
    }

    private static JPanel labeledField(String labelText, Component field) {
        final JPanel panel = new JPanel(new BorderLayout(0, 4));
        panel.add(new JLabel(labelText), BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Sets the controller invoked when the user clicks Save.
     *
     * @param logExpenseController the {@link LogExpenseController}
     */
    public void setLogExpenseController(LogExpenseController logExpenseController) {
        this.logExpenseController = logExpenseController;
    }

    /**
     * Sets the {@link ViewManagerModel} used by the Back button to return to {@link LoggedInView}.
     *
     * @param viewManagerModel the shared view manager model
     */
    public void setViewManagerModel(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
    }

    /** Refreshes the header, status message, form fields, and expense table from {@link AddExpenseState}. */
    @Override
    public void stateChanged() {
        final AddExpenseState state = addExpenseViewModel.getState();
        final String tripName = state.getTripName();
        headerLabel.setText(
                tripName == null || tripName.isBlank() ? "Add Expense" : "Add Expense - " + tripName);

        statusLabel.setText(state.getStatusMessage() == null ? " " : state.getStatusMessage());
        statusLabel.setForeground(state.isError() ? ERROR_COLOR : SUCCESS_COLOR);

        if (state.isClearForm()) {
            descriptionField.setText("");
            amountField.setText("");
            currencyField.setText("");
            dateField.setText("");
            paidByField.setText("");
            categoryBox.setSelectedIndex(0);
        }

        tableModel.setColumnIdentifiers(
                new String[] {"Description", "Paid", "In " + state.getHomeCurrency()});
        tableModel.setRowCount(0);
        for (ExpenseRow row : state.getExpenses()) {
            tableModel.addRow(
                    new Object[] {row.getDescription(), row.getPayer(), row.getAmountInHomeCurrency()});
        }
    }
}