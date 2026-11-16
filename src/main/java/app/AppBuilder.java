package app;

import javax.swing.*;

public class AppBuilder {
    private JPanel panel;
    public JFrame build() {
        final JFrame application = new JFrame("Test");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(panel);


        return application;
    }
}
