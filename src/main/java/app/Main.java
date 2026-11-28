package app;

import java.io.IOException;
import java.nio.file.Path;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Starts PawPortal.
 */
public class Main {

    /** Used when PAWPORTAL_ADMIN_INVITE_CODE is not set, so the team can create admin accounts. */
    private static final String DEFAULT_INVITE_CODE = "pawportal2026";

    /**
     * Returns the folder that holds every saved file: users, pets, preferences and applications.
     *
     * @param configured the folder from PAWPORTAL_DATA_DIR, or null to use ~/.pawportal
     * @return the data folder
     */
    static Path dataDirectory(String configured) {
        if (configured == null || configured.isBlank()) {
            return Path.of(System.getProperty("user.home"), ".pawportal");
        }
        return Path.of(configured.strip());
    }

    /**
     * Opens the main window.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            final Path dataDirectory = dataDirectory(System.getenv("PAWPORTAL_DATA_DIR"));
            try {
                final JFrame frame = AppBuilder.forDataDirectory(dataDirectory, Path.of("."), DEFAULT_INVITE_CODE).build();
                frame.setVisible(true);
            } catch (IOException | IllegalStateException exception) {
                JOptionPane.showMessageDialog(null, "Could not open PawPortal data in " + dataDirectory + ".",
                        "PawPortal", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
