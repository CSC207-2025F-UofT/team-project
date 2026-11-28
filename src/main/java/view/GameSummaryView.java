package view;

import interface_adapter.game_summary.GameSummaryState;
import interface_adapter.game_summary.GameSummaryViewModel;
import interface_adapter.game_summary.GameSummaryViewModelListener;

import javax.swing.*;
import java.awt.*;

public class GameSummaryView extends JPanel implements GameSummaryViewModelListener {

    // Positions of each value in a row: {round, question, team, guess, correct, points}
    // No magic numbers
    private static final int ROUND = 0;
    private static final int QUESTION = 1;
    private static final int TEAM = 2;
    private static final int GUESS = 3;
    private static final int CORRECT = 4;
    private static final int POINTS = 5;

    private final GameSummaryViewModel gameSummaryViewModel;

    private final JLabel resultLabel = new JLabel("[Winner]");
    private final JLabel questionCountLabel = new JLabel("[Questions Played]");
    private final JPanel roundsPanel = new JPanel();

    public GameSummaryView(GameSummaryViewModel gameSummaryViewModel) {
        this.gameSummaryViewModel = gameSummaryViewModel;
        gameSummaryViewModel.addListener(this);

        this.setUpDisplay();
    }

    private void setUpDisplay() {
        this.setSize(1000, 600);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        final JLabel title = new JLabel("Family Feud Summary");
        title.setFont(new Font("Bernard MT Condensed", Font.PLAIN, 48));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultLabel.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 24));
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        questionCountLabel.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 18));
        questionCountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultPanel.add(resultLabel);
        resultPanel.add(questionCountLabel);
        resultPanel.setMaximumSize(new Dimension(1000, 70));

        roundsPanel.setLayout(new BoxLayout(roundsPanel, BoxLayout.Y_AXIS));
        final JScrollPane roundsScrollPane = new JScrollPane(roundsPanel);
        roundsScrollPane.setMaximumSize(new Dimension(900, 380));
        roundsScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // TODO: Add a "Back to Main Menu" button once that is all figured out

        this.add(title);
        this.add(Box.createRigidArea(new Dimension(0, 10)));
        this.add(resultPanel);
        this.add(Box.createRigidArea(new Dimension(0, 10)));
        this.add(roundsScrollPane);
    }

    @Override
    public void stateChanged() {
        GameSummaryState state = gameSummaryViewModel.getState();

        if (state.getTeamWon().isEmpty()) {
            resultLabel.setText("It's a tie!");
        }
        else {
            resultLabel.setText(state.getTeamWon() + " beat " + state.getTeamLost());
        }
        questionCountLabel.setText("Questions played: " + state.getQuestionCount());

        // roundsPanel.removeAll();

        String currentRound = "";
        for (String[] row : state.getRows()) {
            if (!row[ROUND].equals(currentRound)) {
                currentRound = row[ROUND];

                final JLabel roundLabel = new JLabel("Round " + row[ROUND] + ": " + row[QUESTION]);
                roundLabel.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 20));
                roundsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                roundsPanel.add(roundLabel);
            }

            final JLabel guessLabel = new JLabel(
                    row[TEAM] + " guessed \"" + row[GUESS] + "\": " + row[CORRECT] + " (" + row[POINTS] + " pts)");
            guessLabel.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 16));
            roundsPanel.add(guessLabel);
        }

        // roundsPanel.revalidate();
        // roundsPanel.repaint();
    }
}
