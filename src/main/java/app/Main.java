package app;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame application = new AppBuilder()
                    .addMainMenuView()
                    .addGameView()
                    .addStatsView()
                    .addResultView()
                    .addEnterNameUseCase()
                    .addStartRoundUseCase()
                    .addSubmitGuessUseCase()
                    .addFinishRoundUseCase()
                    .addViewStatsUseCase()
                    .build();
            application.setVisible(true);
        });
    }
}
