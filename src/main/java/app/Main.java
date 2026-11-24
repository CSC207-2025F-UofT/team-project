package app;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            final JFrame application = new AppBuilder()
                    .addSignupView()
                    .addLoginView()
                    .addLoggedinView()
                    .addSignupUseCase()
                    .addLoginUseCase()
                    .addGetDataUseCase()
                    .build();

            application.pack();
            application.setLocationRelativeTo(null);
            application.setVisible(true);
        });
    }
}
