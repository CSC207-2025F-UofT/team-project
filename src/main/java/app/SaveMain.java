package app;

import data_access.FileSaveDataAccessObject;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Runs the Save & Leaderboard screen on its own for testing and demos.
 */
public class SaveMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FileSaveDataAccessObject dataAccess = new FileSaveDataAccessObject("saves.csv");

            JFrame frame = new SaveAppBuilder(dataAccess)
                    .addSaveView()
                    .addSaveUseCase()
                    .addLeaderboardUseCase()
                    .build();
            frame.setVisible(true);
        });
    }
}