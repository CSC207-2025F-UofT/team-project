package app;

import javax.swing.*;

public class main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame application = new AppBuilder().build();
            application.pack();
            application.setLocationRelativeTo(null);
            application.setVisible(true);
        });
    }
}
