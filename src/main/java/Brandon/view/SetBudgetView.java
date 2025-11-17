package Brandon.view;

import Brandon.interfaceAdapter.SetBudgetController;
import Brandon.interfaceAdapter.SetBudgetViewModel;

import javax.swing.*;
import java.awt.*;

public class SetBudgetView extends JPanel {

    private final SetBudgetController controller;
    private final SetBudgetViewModel viewModel;

    private final JTextField monthField = new JTextField(10);
    private final JTextField limitField = new JTextField(10);
    private final JTextField totalSpentField = new JTextField(10);
    private final JLabel remainingLabel = new JLabel("");
    private final JLabel messageLabel = new JLabel(" ");

    public SetBudgetView(SetBudgetController controller, SetBudgetViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;

        // Layout
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        int panelWidth = 400;
        int panelHeight = 60;

        JPanel monthPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        monthPanel.setMaximumSize(new Dimension(panelWidth, panelHeight));
        monthPanel.add(new JLabel("Budget Month: "));
        monthPanel.add(monthField);

        JPanel limitPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        limitPanel.setMaximumSize(new Dimension(panelWidth, panelHeight));
        limitPanel.add(new JLabel("Budget Limit: "));
        limitPanel.add(limitField);

        JPanel totalSpentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        totalSpentPanel.setMaximumSize(new Dimension(panelWidth, panelHeight));
        totalSpentPanel.add(new JLabel("Total Spent: "));
        totalSpentPanel.add(totalSpentField);

        JButton setBtn = new JButton("Set Budget");
        setBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        setBtn.addActionListener(e -> handleSetBudget());

        add(Box.createVerticalGlue());   // pushes everything toward center

        add(monthPanel);
        add(limitPanel);
        add(totalSpentPanel);
        add(setBtn);
        add(messageLabel);
        add(remainingLabel);

        add(Box.createVerticalGlue());   // pushes everything toward center
    }

    private void handleSetBudget() {
        String month = monthField.getText();
        float limit;
        float totalSpent;

        try {
            limit = Float.parseFloat(limitField.getText());
        }
        catch (NumberFormatException e) {
            messageLabel.setText("Limit must be a number.");
            return;
        }
        try {
            totalSpent = Float.parseFloat(totalSpentField.getText());
        }
        catch (NumberFormatException e) {
            messageLabel.setText("Total spent must be a number.");
            return;
        }

        controller.setBudget(month, limit, totalSpent);

        messageLabel.setText(viewModel.getMessage());
        remainingLabel.setText("Remaining: $" + viewModel.getRemaining());
    }
}
