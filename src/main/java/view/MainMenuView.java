package view;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import interface_adapter.ViewManagerModel;

/**
 * Main menu: greets the player and offers Play and Statistics.
 * Rule: views only call controllers and only read from view models.
 */
public class MainMenuView extends JPanel {
    public static final String VIEW_NAME = "main menu";

    // TODO (Regan): set from the Enter Name view model ("Welcome, [name]!" or "Welcome back, [name]!")
    private final JLabel greeting = new JLabel("Welcome!");

    public MainMenuView(ViewManagerModel viewManagerModel) {
        JPanel greetingPanel = new JPanel();
        greetingPanel.add(greeting);

        JPanel buttonPanel = new JPanel();
        JButton play = new JButton("Play");
        JButton stats = new JButton("Statistics");
        buttonPanel.add(play);
        buttonPanel.add(stats);

        // TODO (Felix): replace with startRoundController.execute()
        play.addActionListener(e -> {
            viewManagerModel.setState(GameView.VIEW_NAME);
            viewManagerModel.firePropertyChanged();
        });

        // TODO (Nikola): replace with viewStatsController.execute()
        stats.addActionListener(e -> {
            viewManagerModel.setState(StatsView.VIEW_NAME);
            viewManagerModel.firePropertyChanged();
        });

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(greetingPanel);
        add(buttonPanel);
    }
}
