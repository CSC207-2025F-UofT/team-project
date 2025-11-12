package app;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // TODO: Build app
        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                .addSubmitView()
                .build();
        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
        // Here's an example from the CA Lab:
        /*
        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                .addLoginView()
                .addSignupView()
                .addLoggedInView()
                .addSignupUseCase()
                .addLoginUseCase()
                .addChangePasswordUseCase()
                .addLogoutUseCase()
                .build();

        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
         */
    }
}
