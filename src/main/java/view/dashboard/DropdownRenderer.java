package view.dashboard;

import interface_adapter.dashboard.DashboardController;
import interface_adapter.dashboard.DashboardStateCatalog;

import javax.swing.*;
import java.awt.*;

public class DropdownRenderer implements DashboardRenderer<DashboardStateCatalog.DropdownState> {
    private final DashboardController dashboardController;
    private final DropdownEventHandler dropdownEventHandler;

    DropdownRenderer(DashboardController dashboardController) {
        super();
        this.dashboardController = dashboardController;
        this.dropdownEventHandler = new DropdownEventHandler(dashboardController);
    }

    @Override
    public JComponent render(DashboardStateCatalog.DropdownState state) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel prompt = new JLabel(state.label());
        JComboBox<String> dropdown = new JComboBox<>(state.optionLabels().toArray(new String[0]));

        JButton button = new JButton("Go");
        button.addActionListener(e -> {
            String selected = (String)dropdown.getSelectedItem();
            dropdownEventHandler.handleEvent(state.actionId(), new DropdownEventPacket(selected));
        });
        dropdown.setMaximumSize(dropdown.getPreferredSize());

        panel.add(prompt);
        panel.add(dropdown);
        panel.add(button);
        return panel;
    }
}
