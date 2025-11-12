package ui;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;

import javax.swing.*;

public class DashboardFrame extends JFrame {
    
    public DashboardFrame() {
        super("FinWise — Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        // Initialize JavaFX
        final JFXPanel fxPanel = new JFXPanel();
        setContentPane(fxPanel);

        // Create the JavaFX scene on the JavaFX Application Thread
        Platform.runLater(() -> {
            StocksView view = new StocksView();
            fxPanel.setScene(new Scene(view, 1000, 700));
        });
    }
}

