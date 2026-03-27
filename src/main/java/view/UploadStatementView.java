package view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import interface_adapter.upload_statement.UploadStatementController;
import interface_adapter.upload_statement.UploadStatementState;
import interface_adapter.upload_statement.UploadStatementViewModel;
import use_case.upload_statement.UploadStatementInputData;
import interface_adapter.filter_search.FilterSearchController;


public class UploadStatementView extends JPanel implements PropertyChangeListener {

    private final UploadStatementViewModel viewModel;
    private final JLabel totalLabel;
    private final String viewName = "statement view";
    private final JButton analyzeStatementButton;
    private final JButton setSpendingAlertsButton;
    private UploadStatementController uploadStatementController;
    private FilterSearchController filterSearchController;


    public UploadStatementView(UploadStatementViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        this.add(new JLabel("Your total spend for this month: "));
        totalLabel = new JLabel("$0.00");
        this.add(totalLabel);

        final JPanel buttons = new JPanel();
        analyzeStatementButton = new JButton("Analyze Statement");
        buttons.add(analyzeStatementButton);
        analyzeStatementButton.addActionListener(e ->
                this.uploadStatementController.goToCategorizer(new UploadStatementInputData("", viewModel.getState().getUsername()))
        );
        setSpendingAlertsButton = new JButton("Set Spending Alerts");
        buttons.add(setSpendingAlertsButton);
        setSpendingAlertsButton.addActionListener(e ->
                this.uploadStatementController.goToSpendingLimits()
        );
        JButton filterSearchButton = new JButton("Filter Transactions");
        buttons.add(filterSearchButton);

        filterSearchButton.addActionListener(e ->
                this.uploadStatementController.goToFilterSearch()
        );

        this.add(buttons);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        UploadStatementState state = (UploadStatementState) evt.getNewValue();
        totalLabel.setText(String.format("$%.2f", state.getTotalSpend()));
    }

    public void setUploadStatementController(UploadStatementController controller) {
        this.uploadStatementController = controller;
    }

    public void setFilterSearchController(FilterSearchController controller) {
        this.filterSearchController = controller;
    }

    public String getViewName() {
        return viewName;
    }

}
