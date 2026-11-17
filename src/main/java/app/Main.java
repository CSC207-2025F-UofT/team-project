
package app;

import view.AddFoodView;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Entry point for the NutriTrack application.
 *
 * Initializes the application dependencies using AppBuilder
 * and launches the Java Swing user interface.
 */
public class Main {

    /**
     * Starts the NutriTrack application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {

        // Create and display Swing components on the Event Dispatch Thread.
        SwingUtilities.invokeLater(() -> {

            // Build the application's dependencies.
            AppBuilder appBuilder = new AppBuilder();

            // Construct the Add Food view.
            AddFoodView addFoodView = appBuilder.buildAddFoodView();

            // Create the main application window.
            JFrame frame = new JFrame("NutriTrack - Add Food");

            // Close the application when the window is closed.
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Add the Add Food panel to the window.
            frame.setContentPane(addFoodView);

            // Set the initial window size.
            frame.setSize(500, 350);

            // Center the window on the screen.
            frame.setLocationRelativeTo(null);

            // Display the application.
            frame.setVisible(true);
        });
    }
}
