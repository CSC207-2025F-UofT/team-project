package app;

import javax.swing.SwingUtilities;

/** Starts Linked Out. */
public final class Main {
    private Main() {
    }

    /**
     * Builds and shows the main window on the Swing event thread.
     *
     * @param args unused
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AppBuilder()
                .addFindNearbyQuestsUseCase()
                .build()
                .setVisible(true));
    }
}
