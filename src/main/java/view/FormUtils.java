package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Small helpers so every screen looks the same.
 */
final class FormUtils {
    static final Color ERROR_COLOR = new Color(190, 30, 30);
    static final Color NOTICE_COLOR = new Color(30, 130, 60);

    private FormUtils() {
    }

    /** A vertical panel with some padding around it. */
    static JPanel verticalPanel() {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        return panel;
    }

    static JLabel title(String text) {
        final JLabel label = new JLabel(text);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 20f));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    /** One "label: [field]" row. */
    static JPanel row(String labelText, JComponent field) {
        final JPanel row = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        final JLabel label = new JLabel(labelText);
        label.setLabelFor(field);
        row.add(label);
        row.add(field);
        return row;
    }

    /** A centered row of buttons or links. */
    static JPanel centered(JComponent... components) {
        final JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER));
        for (JComponent c : components) {
            row.add(c);
        }
        return row;
    }

    /** A label used for error / notice messages; empty until there is something to show. */
    static JLabel messageLabel() {
        final JLabel label = new JLabel(" ");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    /** Shows an error in red, otherwise a notice in green, otherwise nothing. */
    static void showMessage(JLabel label, String error, String notice) {
        if (error != null) {
            label.setForeground(ERROR_COLOR);
            label.setText(error);
        }
        else if (notice != null) {
            label.setForeground(NOTICE_COLOR);
            label.setText(notice);
        }
        else {
            label.setText(" ");
        }
    }
}
