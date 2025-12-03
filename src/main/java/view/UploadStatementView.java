package view;

import interface_adapter.upload_statement.UploadStatementController;
import interface_adapter.upload_statement.UploadStatementState;
import interface_adapter.upload_statement.UploadStatementViewModel;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class UploadStatementView extends JPanel implements PropertyChangeListener {

    private final UploadStatementViewModel viewModel;
    private final JLabel totalLabel;
    private final String viewName = "statement view";
    private final JButton analyzeStatementButton;
    private final JButton setSpendingAlertsButton;
    private UploadStatementController uploadStatementController;

    public UploadStatementView(UploadStatementViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        this.add(new JLabel("Your total spend for this month: "));
        totalLabel = new JLabel("$0.00");
        this.add(totalLabel);
        final JPanel buttons = new JPanel();
        analyzeStatementButton = new JButton("Analyze Statement");
        buttons.add(analyzeStatementButton);
        setSpendingAlertsButton = new JButton("Set Spending Alerts");
        buttons.add(setSpendingAlertsButton);
        setSpendingAlertsButton.addActionListener(e ->
                this.uploadStatementController.goToSpendingLimits()
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

    public String getViewName() {
        return viewName;
    }

}
