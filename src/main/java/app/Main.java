package app;

import javax.swing.*;

/**
 * The Main class is the entry point of the application.
 */
public class Main {
    public static void main(String[] args) {
        // Use SwingUtilities.invokeLater to ensure thread safety
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
