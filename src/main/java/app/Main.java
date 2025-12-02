package app;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                .addQuizView()
                .addBattleView()
                .addMoveView()
                .addOpenGameView()
                .addResultsView()
                .addAddInventoryView()
                .addUseInventoryView()

                // Use cases
                .addOpenGameUseCase()
                .addMoveUseCase()
                .addBattleUseCase()
                .addQuizUseCase()
                .addResultsUseCase()
                .addAddInventoryUseCase()
                .addUseInventoryUseCase()
                .build();

        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}
