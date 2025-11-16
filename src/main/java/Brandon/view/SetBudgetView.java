package Brandon.view;

import Brandon.interfaceAdapter.SetBudgetController;
import Brandon.interfaceAdapter.SetBudgetViewModel;

import javax.swing.*;

public class SetBudgetView extends JPanel {

    private final SetBudgetController controller;
    private final SetBudgetViewModel viewModel;

    private final JTextField monthField = new JTextField(10);
    private final JTextField limitField = new JTextField(10);
    private final JLabel messageLabel = new JLabel(" ");

    public SetBudgetView(SetBudgetController controller, SetBudgetViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;

        // Layout
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel monthPanel = new JPanel();
        monthPanel.add(new JLabel("Month (e.g., 2025-11): "));
        monthPanel.add(monthField);

        JPanel limitPanel = new JPanel();
        limitPanel.add(new JLabel("Budget Limit: "));
        limitPanel.add(limitField);

        JButton setBtn = new JButton("Set Budget");
        setBtn.addActionListener(e -> handleSetBudget());

        add(monthPanel);
        add(limitPanel);
        add(setBtn);
        add(messageLabel);
    }

    private void handleSetBudget() {
        String month = monthField.getText();
        float limit;

        try {
            limit = Float.parseFloat(limitField.getText());
        }
        catch (NumberFormatException e) {
            messageLabel.setText("Limit must be a number.");
            return;
        }

        controller.setBudget(month, limit);

        messageLabel.setText(viewModel.getMessage());
    }
}
