package app;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        final AppBuilder appBuilder = new AppBuilder();
        final JFrame application = appBuilder
                .addLoginSignupView()
                .addSignupUseCase()
                .build();

        SwingUtilities.invokeLater(() -> {
            application.pack();
            application.setVisible(true);
        });
    }
}
