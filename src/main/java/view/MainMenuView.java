package view;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import interface_adapter.ViewManagerModel;
import interface_adapter.enter_name.welcomeMenu.WelcomeViewModel;
import interface_adapter.enter_name.welcomeMenu.WelcomeViewModelListener;

/**
 * Main menu: greets the player and offers Play and Statistics.
 * The greeting comes from the Enter Name use case's WelcomeViewModel.
 * Rule: views only call controllers and only read from view models.
 */
public class MainMenuView extends JPanel implements WelcomeViewModelListener {
    public static final String VIEW_NAME = WelcomeViewModel.VIEW_NAME;

    private final WelcomeViewModel welcomeViewModel;
    private final JLabel greetingLabel = new JLabel();

    public MainMenuView(ViewManagerModel viewManagerModel, WelcomeViewModel welcomeViewModel) {
        this.welcomeViewModel = welcomeViewModel;
        this.welcomeViewModel.addListener(this);

        JPanel titlePanel = new JPanel();
        titlePanel.add(new JLabel("Welcome to CityGuesser"));

        JPanel greetingPanel = new JPanel();
        greetingPanel.add(greetingLabel);

        JPanel buttonPanel = new JPanel();
        JButton play = new JButton("Play");
        JButton stats = new JButton("Statistics");
        buttonPanel.add(play);
        buttonPanel.add(stats);

        // TODO (Felix): replace with startRoundController.execute()
        play.addActionListener(e -> {
            viewManagerModel.setState(GameView.VIEW_NAME);
            viewManagerModel.notifyViewChanged();
        });

        // TODO (Nikola): replace with viewStatsController.execute()
        stats.addActionListener(e -> {
            viewManagerModel.setState(StatsView.VIEW_NAME);
            viewManagerModel.notifyViewChanged();
        });

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
        greetingLabel.setText(username.isEmpty() ? "Welcome!" : "Hello, " + username + "!");
    }

    public String getViewName() {
        return VIEW_NAME;
    }
}
