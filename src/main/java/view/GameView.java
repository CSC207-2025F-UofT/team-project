package view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import interface_adapter.ViewManagerModel;

/**
 * Game screen: satellite image, guess box, attempts remaining and past guesses (Use Cases 2 and 3).
 * When the Submit Guess view model says the round is won or lost, this view calls the Finish Round controller.
 * Rule: views only call controllers and only read from view models.
 */
public class GameView extends JPanel {
    public static final String VIEW_NAME = "game";

    // TODO (Felix/Elliot): show the image bytes from the view model, e.g. new ImageIcon(bytes)
    private final JLabel imageLabel = new JLabel("[satellite image]", SwingConstants.CENTER);
    private final JLabel attemptsLabel = new JLabel("Attempts remaining: 5");
    private final JLabel feedbackLabel = new JLabel(" ");
    private final JTextField guessField = new JTextField(20);

    public GameView(ViewManagerModel viewManagerModel) {
        imageLabel.setPreferredSize(new Dimension(640, 640));

        JPanel guessPanel = new JPanel();
        guessPanel.add(new JLabel("City:"));
        guessPanel.add(guessField);
        JButton guess = new JButton("Guess");
        guessPanel.add(guess);

        // TODO (Elliot): replace with submitGuessController.execute(guessField.getText())
        guess.addActionListener(e -> feedbackLabel.setText("Guessed: " + guessField.getText() + " (dummy)"));

        // Dummy button so the flow can be clicked through. Remove once Submit Guess ends rounds.
        // TODO (Ingrid): the real flow calls finishRoundController.execute() when the round is over
        JButton endRound = new JButton("End round (dummy)");
        endRound.addActionListener(e -> {
            viewManagerModel.setState(ResultView.VIEW_NAME);
            viewManagerModel.firePropertyChanged();
        });

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(attemptsLabel);
        infoPanel.add(feedbackLabel);
        infoPanel.add(guessPanel);
        infoPanel.add(endRound);

        setLayout(new BorderLayout());
        add(imageLabel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.SOUTH);
    }
}
