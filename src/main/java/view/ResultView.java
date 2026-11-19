package view;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import interface_adapter.ViewManagerModel;

/**
 * Result popup shown when a round ends: city, points earned, and what to do next (Use Case 4).
 * Rule: views only call controllers and only read from view models.
 */
public class ResultView extends JDialog {
    public static final String VIEW_NAME = "result";

    // TODO (Ingrid): fill from the Finish Round view model (city, points, "not saved" warning)
    private final JLabel resultLabel = new JLabel("Round over!");

    public ResultView(JFrame owner, ViewManagerModel viewManagerModel) {
        super(owner, "Round result", true);

        JButton playAgain = new JButton("Play again");
        JButton viewStats = new JButton("View stats");
        JButton mainMenu = new JButton("Main menu");

        // TODO (Felix): replace with startRoundController.execute()
        playAgain.addActionListener(e -> switchTo(viewManagerModel, GameView.VIEW_NAME));
        // TODO (Nikola): replace with viewStatsController.execute()
        viewStats.addActionListener(e -> switchTo(viewManagerModel, StatsView.VIEW_NAME));
        mainMenu.addActionListener(e -> switchTo(viewManagerModel, MainMenuView.VIEW_NAME));

        JPanel labelPanel = new JPanel();
        labelPanel.add(resultLabel);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(playAgain);
        buttonPanel.add(viewStats);
        buttonPanel.add(mainMenu);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(labelPanel);
        content.add(buttonPanel);
        setContentPane(content);
        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        pack();
    }

    private static void switchTo(ViewManagerModel viewManagerModel, String viewName) {
        viewManagerModel.setState(viewName);
        viewManagerModel.firePropertyChanged();
    }
}
