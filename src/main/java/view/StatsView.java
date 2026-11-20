package view;

import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import interface_adapter.ViewManagerModel;

/**
 * Statistics screen: points, games played, win %, streaks and guess distribution (Use Case 5).
 * Rule: views only call controllers and only read from view models.
 */
public class StatsView extends JPanel {
    public static final String VIEW_NAME = "stats";

    // TODO (Nikola): fill these from the View Stats view model
    private final JLabel pointsLabel = new JLabel("Total points: 0");
    private final JLabel gamesLabel = new JLabel("Games played: 0");
    private final JLabel winLabel = new JLabel("Win %: 0");
    private final JLabel currentStreakLabel = new JLabel("Current streak: 0");
    private final JLabel longestStreakLabel = new JLabel("Longest streak: 0");

    public StatsView(ViewManagerModel viewManagerModel) {
        JPanel numbers = new JPanel(new GridLayout(0, 1));
        numbers.add(pointsLabel);
        numbers.add(gamesLabel);
        numbers.add(winLabel);
        numbers.add(currentStreakLabel);
        numbers.add(longestStreakLabel);

        JPanel distribution = new JPanel(new GridLayout(0, 1));
        distribution.add(new JLabel("Guess distribution:"));
        for (int attempt = 1; attempt <= 5; attempt++) {
            distribution.add(new JLabel(attempt + ": 0"));
        }

        // TODO (Nikola): go back to whichever screen opened the stats (main menu or result window)
        JButton back = new JButton("Back");
        back.addActionListener(e -> {
            viewManagerModel.setState(MainMenuView.VIEW_NAME);
            viewManagerModel.firePropertyChanged();
        });

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(numbers);
        add(distribution);
        add(back);
    }
}
