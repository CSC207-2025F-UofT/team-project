package view;

import interface_adapter.spending_limits.SpendingLimitsController;
import interface_adapter.spending_limits.SpendingLimitsViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static common.Constants.CATEGORIES;

public class SpendingLimitsView extends JPanel implements PropertyChangeListener {

    private final SpendingLimitsViewModel viewModel;
    private SpendingLimitsController controller;

    private final HashMap<String, JFormattedTextField> userInputFields = new HashMap<>();
    private final JButton saveButton;
    private final String viewName = "spending limits";

    public SpendingLimitsView(SpendingLimitsViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        this.add(new JLabel("Set your spending limits for each category:"));

        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(0);
        format.setGroupingUsed(false);

        for (String category : CATEGORIES) {
            JPanel categoryPanel = new JPanel();
            categoryPanel.add(new JLabel(category));
            JFormattedTextField field = new JFormattedTextField(format);
            field.setPreferredSize(new Dimension(300, 30));
            categoryPanel.add(field);
            this.add(categoryPanel);
            userInputFields.put(category, field);
        }

        JPanel buttons = new JPanel();
        saveButton = new JButton("Save");
        saveButton.addActionListener(e -> saveLimits());
        buttons.add(saveButton);
        this.add(buttons);
    }

    private void saveLimits() {
        Map<String, Double> limits = new HashMap<>();
        for (String category : CATEGORIES) {
            JFormattedTextField field = userInputFields.get(category);
            if (field.getValue() != null) {
                limits.put(category, ((Number) field.getValue()).doubleValue());
            }
        }

        controller.saveLimits(viewModel.getState().getUsername(), limits);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
    }

    public void setController(SpendingLimitsController controller) { this.controller = controller; }

    public String getViewName() {
        return viewName;
    }
}

