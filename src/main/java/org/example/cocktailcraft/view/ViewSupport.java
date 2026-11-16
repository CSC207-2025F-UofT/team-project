package org.example.cocktailcraft.view;

import org.example.cocktailcraft.interface_adapter.MessageState;
import org.example.cocktailcraft.interface_adapter.ViewModel;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/** Shared visual plumbing only. No business rules or data access. */
final class ViewSupport {
    private ViewSupport() {
    }

    static JPanel column() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        return panel;
    }

    static JButton button(String label, Runnable action) {
        JButton button = new JButton(label);
        button.addActionListener(event -> action.run());
        return button;
    }

    static JLabel status(ViewModel<? extends MessageState> model) {
        JLabel label = new JLabel(model.getState().getMessage());
        model.addListener(() -> label.setText(model.getState().getMessage()));
        return label;
    }
}
