package view;

import java.awt.Component;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/** Renders organization attribution and opens listing links in the system browser. */
final class ExternalLink {
    private ExternalLink() {
    }

    static JComponent organization(String name, String website) {
        if (website.isEmpty()) {
            final JLabel label = new JLabel();
            label.putClientProperty("html.disable", true);
            label.setText(name);
            return label;
        }
        final JButton websiteButton = new JButton();
        websiteButton.putClientProperty("html.disable", true);
        websiteButton.setText(name);
        websiteButton.setToolTipText(website);
        websiteButton.addActionListener(event -> open(websiteButton, website));
        return websiteButton;
    }

    static void open(Component parent, String url) {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(URI.create(url));
                return;
            }
        } catch (IOException | IllegalArgumentException exception) {
            // The address is still available for the visitor to copy.
        }
        JOptionPane.showInputDialog(parent, "Open this address in your browser.", url);
    }
}
