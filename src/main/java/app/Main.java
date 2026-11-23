package app;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Starts PawPortal.
 */
public class Main {

    /**
     * Opens the main window.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            final JFrame frame = new AppBuilder().addGalleryView().addApplicationViews().build();
            frame.setVisible(true);
        });
    }
}
