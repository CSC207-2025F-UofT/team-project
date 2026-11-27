import app.AppBuilder;

import javax.swing.*;

/**
 * Starts the UNO Desktop application.
 */
public class Main {

    /**
     * Builds the application window and shows it.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        JFrame application = new AppBuilder()
                .addLoginView()
                .addSignupView()
                .addLobbyView()
                .addViewPlayerStatsView()
                .addLoginUseCase()
                .addSignupUseCase()
                .build();

        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}
