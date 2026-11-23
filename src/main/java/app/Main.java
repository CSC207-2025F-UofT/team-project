package app;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/** Starts the game. All wiring lives in AppBuilder. */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame application = new AppBuilder()
                    .addGameView()
                    .addDialogueView()
                    .addNewGameUseCase()
                    .addTalkToNpcUseCase()
                    .addLoginView()
                    .addSignupView()
                    .addLoginUseCase()
                    .addSignupUseCase()
                    .addLogoutUseCase()
                    .build();
            application.setSize(480, 360);
            application.setLocationRelativeTo(null);
            application.setVisible(true);
        });
    }
}
