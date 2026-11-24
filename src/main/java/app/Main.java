package app;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                .addGameRunningView()
                .addLoadQuestionUseCase()
                .build();

        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}
