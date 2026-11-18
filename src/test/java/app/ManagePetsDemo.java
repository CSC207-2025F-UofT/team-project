package app;

import java.nio.file.Path;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import data_access.FilePetDataAccessObject;
import view.ManagePetView;

/** Runs the admin pet management MVP in isolation for testing and demos. */
public final class ManagePetsDemo {
    private static final String TITLE = "PawPortal - Pet Management Demo";

    private ManagePetsDemo() {
    }

    /**
     * Opens the demo using a local pet data file.
     * @param args optional first argument for the local JSON file path
     */
    public static void main(String[] args) {
        final Path dataPath = Path.of(args.length == 0 ? "pets-demo.json" : args[0]);
        SwingUtilities.invokeLater(() -> start(dataPath));
    }

    private static void start(Path dataPath) {
        try {
            final FilePetDataAccessObject petDataAccess = new FilePetDataAccessObject(dataPath);
            final ManagePetView petManagement = new AppBuilder().buildPetManagement(petDataAccess);
            final JFrame frame = new JFrame(TITLE);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.add(petManagement);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
        catch (IllegalStateException exception) {
            JOptionPane.showMessageDialog(null, "Could not load the pet listings from " + dataPath + ".",
                    TITLE, JOptionPane.ERROR_MESSAGE);
        }
    }
}
