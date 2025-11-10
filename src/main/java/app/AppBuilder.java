package app;

import javax.swing.*;

public class AppBuilder {
    public JFrame build() {
        final JFrame application = new JFrame("Recipe Application");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        return application;
    }
}
