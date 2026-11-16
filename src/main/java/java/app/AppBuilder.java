package app;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    // other instance attributes as needed

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public JFrame build() {
        final JFrame application = new JFrame("Text Editor");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        // Add code similar to below:
        // viewManagerModel.setState(SignupViewModel.VIEW_NAME);
        // viewManagerModel.notifyViewChanged();

        return application;
    }

}