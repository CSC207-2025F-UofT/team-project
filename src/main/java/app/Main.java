
package app;

import view.AddFoodView;
import view.FoodBankView;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

/**
 * Entry point for the NutriTrack application.
 *
 * Builds the application's dependencies and launches
 * the Java Swing user interface.
 *
 * The application currently provides two tabs:
 * Add Food and Food Bank.
 *
 * Both use cases share the same in-memory repository
 * through AppBuilder.
 */
public class Main {

    /**
     * Starts the NutriTrack application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {

        // Create and update Swing components on the Event Dispatch Thread.
        SwingUtilities.invokeLater(() -> {

            // Build the application and its shared repository.
            AppBuilder appBuilder = new AppBuilder();

            // Construct both feature views.
            AddFoodView addFoodView =
                    appBuilder.buildAddFoodView();

            FoodBankView foodBankView =
                    appBuilder.buildFoodBankView();

            // Create the tabbed interface.
            JTabbedPane tabbedPane = new JTabbedPane();

            tabbedPane.addTab("Add Food", addFoodView);
            tabbedPane.addTab("Food Bank", foodBankView);

            /*
             * Refresh the Food Bank whenever its tab is selected.
             *
             * This ensures that foods added through Add Food
             * become visible without restarting the application.
             */
            tabbedPane.addChangeListener(event -> {

                if (tabbedPane.getSelectedComponent() == foodBankView) {
                    foodBankView.refreshFoodBank();
                }
            });

            // Create the main application window.
            JFrame frame = new JFrame("NutriTrack");

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(tabbedPane);

            // Provide enough space for the nutritional table.
            frame.setSize(800, 450);

            // Center the application window.
            frame.setLocationRelativeTo(null);

            // Display the application.
            frame.setVisible(true);
        });
    }
}
