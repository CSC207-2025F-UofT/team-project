package view;

import interface_adapter.filter_search.FilterSearchController;
import interface_adapter.filter_search.FilterSearchState;
import interface_adapter.filter_search.FilterSearchViewModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class FilterSearchView extends JPanel implements PropertyChangeListener {

    private FilterSearchController controller;
    private final FilterSearchViewModel viewModel;

    private final JTextField startDateField = new JTextField(10);
    private final JTextField endDateField = new JTextField(10);
    private final JTextField merchantField = new JTextField(15);

    private final JComboBox<String> categoryBox =
            new JComboBox<>(new String[]{
                    "All",
                    "GROCERIES",
                    "SHOPPING",
                    "DINING OUT",
                    "ENTERTAINMENT",
                    "UTILITIES",
                    "TRANSPORTATION",
                    "FITNESS",
                    "MISCELLANEOUS",
                    "ERRNOKEY"
            });

    private final JTable resultsTable;
    private final DefaultTableModel tableModel;

    private final JButton filterButton = new JButton("Apply Filter");

    private final String viewName = "filter search";

    public FilterSearchView(FilterSearchViewModel viewModel) {
        this.viewModel = viewModel;
        viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());

        // ---------- FILTER PANEL ----------
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new GridLayout(3, 4, 10, 10));

        filterPanel.add(new JLabel("Category:"));
        filterPanel.add(categoryBox);

        filterPanel.add(new JLabel("Start Date (YYYY-MM-DD):"));
        filterPanel.add(startDateField);

        filterPanel.add(new JLabel("End Date (YYYY-MM-DD):"));
        filterPanel.add(endDateField);

        filterPanel.add(new JLabel("Merchant Keyword:"));
        filterPanel.add(merchantField);

        filterPanel.add(new JLabel(""));
        filterPanel.add(filterButton);

        add(filterPanel, BorderLayout.NORTH);

        // ---------- TABLE ----------
        tableModel = new DefaultTableModel(
                new Object[]{"Date", "Merchant", "Amount", "Category"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false; // Make table non-editable
            }
        };

        resultsTable = new JTable(tableModel);
        resultsTable.setFillsViewportHeight(true);

        add(new JScrollPane(resultsTable), BorderLayout.CENTER);

        // ---------- EVENT HANDLER ----------
        filterButton.addActionListener(e ->
                controller.execute(
                        (String) categoryBox.getSelectedItem(),
                        startDateField.getText(),
                        endDateField.getText(),
                        merchantField.getText()
                )
        );
    }

    // ---------- SETTER ----------
    public void setController(FilterSearchController controller) {
        this.controller = controller;
    }

    public String getViewName() {
        return viewName;
    }

    // ---------- VIEWMODEL LISTENER ----------
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        FilterSearchState state = (FilterSearchState) evt.getNewValue();

        tableModel.setRowCount(0); // Clear table

        if (state.getError() != null) {
            JOptionPane.showMessageDialog(
                    this,
                    state.getError(),
                    "Filter Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (state.getFiltered() != null) {
            state.getFiltered().forEach(t ->
                    tableModel.addRow(new Object[]{
                            t.getDate(),
                            t.getMerchant(),
                            t.getAmount(),
                            t.getCategory()
                    })
            );
        }
    }
}
