package app;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                .addLoginView()
                .addBlankView()
                .addSignupView()
                .addLoginUseCase()
                .addSignupUseCase()
                .build();

        application.pack();
        application.setSize(800, 600);
        application.setVisible(true);
    }
}
