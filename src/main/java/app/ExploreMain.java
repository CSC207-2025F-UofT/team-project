package app;

import data_access.InMemoryExploreDataAccessObject;
import data_access.DungeonFactory;
import entity.Inventory;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Runs the Explore screen on its own for testing and demos.
 * This is the class to run in IntelliJ (it has the main method).
 */
public class ExploreMain {
    /** Small on purpose, so the "inventory full" case is easy to try. */
    private static final int INVENTORY_CAPACITY = 3;

    /**
     * Starts the program.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        // Swing rule: create and change UI components only on the Swing event thread.
        SwingUtilities.invokeLater(() -> {
            // Game state: the sample floor plus an empty inventory.
            InMemoryExploreDataAccessObject dataAccess = new InMemoryExploreDataAccessObject(
                    DungeonFactory.createFloorOne(), new Inventory(INVENTORY_CAPACITY));

            // Build the window step by step, then show it.
            JFrame frame = new ExploreAppBuilder(dataAccess)
                    .addExploreView()
                    .addPlaceholderCombatView()
                    .addExploreUseCases()
                    .build();
            frame.setVisible(true);
        });
    }
}
