package view;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import interface_adapter.ViewManagerModel;

/**
 * Welcome screen: the player types a name and continues (Use Case 1).
 * Rule: views only call controllers and only read from view models.
 */
public class WelcomeView extends JPanel {
    public static final String VIEW_NAME = "welcome";

    private final JTextField usernameField = new JTextField(20);

    public WelcomeView(ViewManagerModel viewManagerModel) {
        JPanel welcomePanel = new JPanel();
        welcomePanel.add(new JLabel("Welcome to CityGuesser"));

        JPanel usernamePanel = new JPanel();
        usernamePanel.add(new JLabel("Username:"));
        usernamePanel.add(usernameField);

        JPanel buttonPanel = new JPanel();
        JButton play = new JButton("Play");
        buttonPanel.add(play);

        // TODO (Regan): replace with enterNameController.execute(usernameField.getText())
        play.addActionListener(e -> {
            viewManagerModel.setState(MainMenuView.VIEW_NAME);
            viewManagerModel.firePropertyChanged();
        });

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(welcomePanel);
        add(usernamePanel);
        add(buttonPanel);
    }
}
