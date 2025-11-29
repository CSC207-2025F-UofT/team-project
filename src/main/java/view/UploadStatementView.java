package view;

import interface_adapter.upload_statement.UploadStatementState;
import interface_adapter.upload_statement.UploadStatementViewModel;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class UploadStatementView extends JPanel implements PropertyChangeListener {

    private final UploadStatementViewModel viewModel;
    private final JLabel totalLabel;
    private final String viewName = "statement view";

    public UploadStatementView(UploadStatementViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        this.add(new JLabel("Your total spend for this month: "));
        totalLabel = new JLabel("$0.00");
        this.add(totalLabel);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        UploadStatementState state = (UploadStatementState) evt.getNewValue();
        totalLabel.setText(String.format("$%.2f", state.getTotalSpend()));
    }

    public String getViewName() {
        return viewName;
    }

}
