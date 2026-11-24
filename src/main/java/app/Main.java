
package app;

import view.AddFoodView;

import javax.swing.*;

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

            JTabbedPane tabs = new JTabbedPane();
            tabs.addTab("Add Food", appBuilder.buildAddFoodView());
            tabs.addTab("Create Plan", appBuilder.buildCreatePlanView());
            tabs.addTab("Add Planned Meal", appBuilder.buildAddPlannedMealView());


            // Create the main application window.
            JFrame frame = new JFrame("NutriTrack");

            // Close the application when the window is closed.
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Add the Add Food panel to the window.
            frame.setContentPane(tabs);

            // Set the initial window size.
            frame.setSize(500, 350);

            // Center the window on the screen.
            frame.setLocationRelativeTo(null);

            // Display the application.
            frame.setVisible(true);
        });
    }
}
