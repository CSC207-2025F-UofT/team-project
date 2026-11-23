package View;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import interface_adapter.welcomeMenu.WelcomeViewModel;
import interface_adapter.welcomeMenu.WelcomeViewModelListener;


/**
 * The view shown after a player enters their username.
 */
public class WelcomeView extends JPanel implements WelcomeViewModelListener {

    private final WelcomeViewModel welcomeViewModel;
    private final JLabel greetingLabel = new JLabel();

    public WelcomeView(WelcomeViewModel welcomeViewModel) {
        this.welcomeViewModel = welcomeViewModel;
        this.welcomeViewModel.addListener(this);

        final JPanel titlePanel = new JPanel();
        titlePanel.add(new JLabel("Welcome to CityGuesser"));

        final JPanel greetingPanel = new JPanel();
        greetingPanel.add(greetingLabel);

        final JPanel buttonPanel = new JPanel();
        buttonPanel.add(new JButton("Play"));
        buttonPanel.add(new JButton("View Stats"));

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(titlePanel);
        add(greetingPanel);
        add(buttonPanel);

        updateGreeting();
    }

    @Override
    public void stateChanged() {
        updateGreeting();
    }

    private void updateGreeting() {
        final String username = welcomeViewModel.getState().getUsername();
        greetingLabel.setText("Hello, " + username + "!");
    }

    public String getViewName() {
        return WelcomeViewModel.VIEW_NAME;
    }
}
