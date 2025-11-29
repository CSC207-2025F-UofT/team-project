package view;

import interface_adapter.filter_search.FilterSearchController;
import interface_adapter.filter_search.FilterSearchViewModel;
import interface_adapter.filter_search.FilterSearchState;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class FilterSearchView extends JPanel {

    private final FilterSearchController controller;
    private final FilterSearchViewModel viewModel;

    // UI fields
    private final JTextField searchTextField = new JTextField(15);
    private final JTextField categoryField = new JTextField(15);
    private final JTextField startDateField = new JTextField(10);  // yyyy-MM-dd
    private final JTextField endDateField = new JTextField(10);    // yyyy-MM-dd
    private final JTextField minAmountField = new JTextField(10);
    private final JTextField maxAmountField = new JTextField(10);

    private final JButton searchButton = new JButton("Search");
    private final JTable resultsTable = new JTable();

    public FilterSearchView(FilterSearchController controller,
                            FilterSearchViewModel viewModel) {

        this.controller = controller;
        this.viewModel = viewModel;

        setLayout(new BorderLayout());

        // ---- Top Panel: Inputs ----
        JPanel inputPanel = new JPanel(new GridLayout(3, 4, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Filter Options"));

        inputPanel.add(new JLabel("Search Text:"));
        inputPanel.add(searchTextField);

        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(categoryField);

        inputPanel.add(new JLabel("Start Date (yyyy-MM-dd):"));
        inputPanel.add(startDateField);

        inputPanel.add(new JLabel("End Date (yyyy-MM-dd):"));
        inputPanel.add(endDateField);

        inputPanel.add(new JLabel("Min Amount:"));
        inputPanel.add(minAmountField);

        inputPanel.add(new JLabel("Max Amount:"));
        inputPanel.add(maxAmountField);

        inputPanel.add(searchButton);

        add(inputPanel, BorderLayout.NORTH);

        // ---- Table Panel ----
        resultsTable.setModel(new DefaultTableModel(
                new Object[]{"Date", "Category", "Amount", "Description"},
                0
        ));

        add(new JScrollPane(resultsTable), BorderLayout.CENTER);

        // ---- Listeners ----
        searchButton.addActionListener(e -> triggerSearch());

        // Initial render
        refreshView();
    }

    private void triggerSearch() {
        try {
            String text = searchTextField.getText();
            String category = categoryField.getText();

            LocalDate start = startDateField.getText().isEmpty() ? null :
                    LocalDate.parse(startDateField.getText());

            LocalDate end = endDateField.getText().isEmpty() ? null :
                    LocalDate.parse(endDateField.getText());

            Double min = minAmountField.getText().isEmpty() ? null :
                    Double.parseDouble(minAmountField.getText());

            Double max = maxAmountField.getText().isEmpty() ? null :
                    Double.parseDouble(maxAmountField.getText());

            controller.execute(text, category, start, end, min, max);

            refreshView();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid input format.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshView() {
        FilterSearchState state = viewModel.getState();

        if (state.getErrorMessage() != null) {
            JOptionPane.showMessageDialog(this,
                    state.getErrorMessage(),
                    "Search Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Update table
        if (state.getResults() != null) {

            DefaultTableModel model = (DefaultTableModel) resultsTable.getModel();
            model.setRowCount(0); // clear table

            state.getResults().forEach(tx -> model.addRow(new Object[]{
                    tx.getDate(),
                    tx.getCategory(),
                    tx.getAmount(),
                    tx.getDescription()
            }));
        }
    }
}
