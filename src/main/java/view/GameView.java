package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.MalformedURLException;
import java.net.URI;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

import interface_adapter.game.GameState;
import interface_adapter.game.GameViewModel;
import interface_adapter.submit_guess.SubmitGuessController;
import interface_adapter.start_round.StartRoundController;

public class GameView extends JPanel implements PropertyChangeListener {
    private final GameViewModel viewModel;
    private final SubmitGuessController submitGuessController;

    private final JLabel imageLabel = new JLabel("", SwingConstants.CENTER);
    private final JPanel imageButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
    private final JLabel messageLabel = new JLabel(" ", SwingConstants.CENTER);
    private final JLabel attemptsLabel = new JLabel(" ");
    private final JLabel scoreLabel = new JLabel(" ");
    private final JTextField guessField = new JTextField(20);
    private final JButton guessButton = new JButton("Guess");
    private final JButton playAgainButton = new JButton("Play again");

    private String currentImageURL = "";

    public GameView(
            GameViewModel viewModel,
            StartRoundController startRoundController,
            SubmitGuessController submitGuessController,
            int imageSize
    ) {
        this.viewModel = viewModel;
        this.submitGuessController = submitGuessController;


        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel top = new JPanel(new BorderLayout());
        top.add(attemptsLabel, BorderLayout.WEST);
        top.add(scoreLabel, BorderLayout.EAST);
        top.add(messageLabel, BorderLayout.SOUTH);

        imageLabel.setPreferredSize(new Dimension(imageSize, imageSize));
        imageLabel.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 8));
        imageButtons.setOpaque(false);
        imageLabel.add(imageButtons);

        JPanel bottom = new JPanel();
        bottom.add(guessField);
        bottom.add(guessButton);
        bottom.add(playAgainButton);

        this.add(top, BorderLayout.NORTH);
        this.add(imageLabel, BorderLayout.CENTER);
        this.add(bottom, BorderLayout.SOUTH);

        guessButton.addActionListener(e -> submitGuess());
        guessField.addActionListener(e -> submitGuess());
//        playAgainButton.addActionListener(e -> startRoundController.execute());


        viewModel.addPropertyChangeListener(this);
    }

    private void submitGuess() {
//        submitGuessController.execute(guessField.getText());
        guessField.setText("");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        GameState state = viewModel.getState();

        messageLabel.setText(state.getMessage());
        attemptsLabel.setText("Attempts left: " + state.getAttemptsLeft());
        scoreLabel.setText(state.isRoundOver() ? "Score: " + state.getScore() : " ");

        if (!currentImageURL.equals(state.getImageURL())) {
            currentImageURL = state.getImageURL();
            showImage(currentImageURL);
        }

        updateImageButtons(state);

        guessField.setEnabled(!state.isRoundOver());
        guessButton.setEnabled(!state.isRoundOver());
        playAgainButton.setVisible(state.isRoundOver());
    }

    private void updateImageButtons(GameState state) {
        imageButtons.removeAll();

        for (int i = 0; i < state.getImageCount(); i++) {
            int index = i;
            JToggleButton button = new JToggleButton(String.valueOf(i + 1));
            button.setPreferredSize(new Dimension(32, 24));
            button.setMargin(new Insets(0, 0, 0, 0));
            button.setSelected(i == state.getSelectedImage());
            button.setFocusable(false);
            button.addActionListener(e -> {
                viewModel.getState().selectImage(index);
                viewModel.firePropertyChanged();
            });
            imageButtons.add(button);
        }

        imageButtons.revalidate();
        imageButtons.repaint();
    }

    private void showImage(String imageURL) {
        try {
            imageLabel.setIcon(
                    new ImageIcon(
                            URI.create(imageURL).toURL()
                    )
            );
        } catch (MalformedURLException | IllegalArgumentException e) {
            imageLabel.setIcon(null);
            imageLabel.setText("Could not load image");
        }
    }

}
