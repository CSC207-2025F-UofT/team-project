package app;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // TODO: Build complete app

        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                .addLoginView()
                .build();

        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}
