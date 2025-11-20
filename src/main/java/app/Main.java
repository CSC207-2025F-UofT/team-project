package app;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                .addBrowseLandmarksView()
                .addHomescreenView()
                .addLoginView()
                .addSignupView()
                .addLoginUseCase()
                .addSignupUseCase()
                .addHomescreenUseCase()
                .build();

        application.pack();
        application.setSize(800, 600);
        application.setVisible(true);
    }
}
