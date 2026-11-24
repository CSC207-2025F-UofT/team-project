package view;

import entities.Game.CandleGame;
import entities.Game.CandleType;
import entities.Game.GameCondition;
import entities.Game.Guess;
import interface_adapters.candle_game.CandleGameViewModel;
import interface_adapters.candle_game.CandleGameViewModelListener;
import javax.swing.*;
//import interface_adapters.candle_game.CandleGameController;
import interface_adapters.candle_game.EvaluateGuessController;
import interface_adapters.candle_game.EvaluateGuessPresenter;
import use_case.use_case_EvaluateGuess.EvaluateGuessInteractor;
import java.util.List;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CandleGameView extends JPanel implements ActionListener, CandleGameViewModelListener {

    // TODO add home view model
    private final CandleGameViewModel candleGameViewModel;
    private final EvaluateGuessController controller;
    private final CandleGame game;

    private final JLabel[][] attempts;
    private final JLabel attemptsRemaining;
    private final JLabel background;
    private final JLabel[] candleFlames;
    private final JTextField[] numberBoxes;
    private final ImageIcon brightCandle = new ImageIcon("src/images/ORANGE_FLAME.png");
    private final ImageIcon dimCandle = new ImageIcon("src/images/RED_FLAME.png");

    private final JButton quitButton;
    private final JButton instructionsButton;
    private final JButton submitButton;

    /**
     * Constructs the Candle Game View
     * Initializes all it's components, and connects the View to the Controller
     *
     * @param controller the Controller for CandleGame
     */
    public CandleGameView(EvaluateGuessController controller, CandleGameViewModel candleGameViewModel, CandleGame game) {
        this.controller = controller;
        this.candleGameViewModel = candleGameViewModel;
        this.game = game;
        this.candleGameViewModel.addListener(this);

        this.setLayout(null);

        // TODO call
//        FontManager.getWinkle(48f)


        //Attempts Remaining Tracker
        attemptsRemaining = new JLabel("10");
        attemptsRemaining.setBounds(75, 80, 500, 35);
        attemptsRemaining.setFont(FontManager.getWinkle(48f));
        this.add(attemptsRemaining);


        // Quit Button
        quitButton = new JButton("QUIT");
        quitButton.setBounds(10, 10, 100, 50);
        quitButton.setFont(FontManager.getWinkle(30f));
        quitButton.setBackground(Color.decode("#362514"));
        quitButton.setForeground(Color.WHITE);
        quitButton.addActionListener(this);
        this.add(quitButton);

        // Instructions Button
        instructionsButton = new JButton("?");
        instructionsButton.setBounds(10, 70, 50, 50);
        instructionsButton.setFont(FontManager.getWinkle(30f));
        instructionsButton.setBackground(Color.decode("#362514"));
        instructionsButton.setForeground(Color.WHITE);
        instructionsButton.addActionListener(this);
        this.add(instructionsButton);

        // Submit Button
        submitButton = new JButton("SUBMIT");
        submitButton.setBounds(340, 460, 120, 50);
        submitButton.setFont(FontManager.getWinkle(30f));
        submitButton.setBackground(Color.decode("#362514"));
        submitButton.setForeground(Color.WHITE);
        submitButton.addActionListener(this);
        this.add(submitButton);

        // Background
        background = new JLabel(new ImageIcon("src/images/CANDLE_GAME_BG.png"));
        background.setBounds(0, 0, 800, 600);
        this.add(background);
        // Make sure it is the bottom Layer
        this.setComponentZOrder(background, this.getComponentCount() - 1);

        // Number Boxes
        numberBoxes = new JTextField[5];
        for (int i = 0; i < numberBoxes.length; i++) {
            numberBoxes[i] = new JTextField();
            numberBoxes[i].setBounds(70 + (i * 150), 370, 60, 30);
            numberBoxes[i].setFont(FontManager.getWinkle(30f));
            numberBoxes[i].setHorizontalAlignment(JTextField.CENTER);
            this.add(numberBoxes[i]);
        }

        // Candle Flames
        candleFlames = new JLabel[5];
        for (int i = 0; i < candleFlames.length; i++) {
            candleFlames[i] = new JLabel();
            candleFlames[i].setBounds(3 + (i * 150), 250, 150, 100);
            this.add(candleFlames[i]);
            this.setComponentZOrder(background, this.getComponentCount() - 1);
        }

        //Attempts Recorded
        attempts = new JLabel[10][5];
        for (int i = 0; i < attempts.length; i++) {
            for (int c = 0; c < attempts[i].length; c++) {
                attempts[i][c] = new JLabel();
                attempts[i][c].setBounds(720 + (c * 10), 20 + (i * 20), 200, 20);
                attempts[i][c].setFont(FontManager.getWinkle(30f));
                this.add(attempts[i][c]);
            }
        }
        this.setComponentZOrder(background, this.getComponentCount() - 1);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == instructionsButton) {
            instructionsOpen();
        }
        else if (e.getSource() == quitButton) {
            // Needs to bring back to home view but will implement later
            System.out.println("Quit button clicked!");
        }
        else if (e.getSource() == submitButton) {
            StringBuilder guess = new StringBuilder();
            for (JTextField box : numberBoxes) {
                guess.append(box.getText());
            }
            // Needs to keep submission
            controller.execute(guess.toString(), game);
        }
    }


    private void instructionsOpen() {
        JFrame instructionsFrame = new JFrame("Instructions for Light it Up!");
        instructionsFrame.setMinimumSize(new java.awt.Dimension(400, 300));

        JLabel instructionsTitle = new JLabel("How to Play Light it Up");
        JLabel instructions = new JLabel("These are where the instructions will go");
        instructionsTitle.setFont(FontManager.getWinkle(20f));
        instructions.setFont(FontManager.getWinkle(10f));
        instructionsTitle.setBounds(100, 10, 200, 20);
        instructions.setBounds(10, 0, 100, 20);

        instructionsFrame.add(instructionsTitle);
        instructionsFrame.add(instructions);
        instructionsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        instructionsFrame.setLocationRelativeTo(this);
        instructionsFrame.setVisible(true);
    }

    @Override
    public void candleGameStateChanged() {
        updateCandles(candleGameViewModel.getCandleFeedback());
        attemptsRemaining.setText(String.valueOf(candleGameViewModel.getAttemptsRemaining()));
        if (game.getGameCondition() != GameCondition.WON) {
            printPastGuesses(candleGameViewModel.getPastGuesses());
        }
    }

    private void updateCandles(List<CandleType> feedback) {
        for (int i = 0; i < feedback.size(); i++) {
            if (feedback.get(i) == CandleType.BRIGHT) {
                candleFlames[i].setIcon(brightCandle);
                System.out.println("Setting candle " + i + " to orange");
            } else if (feedback.get(i) == CandleType.DIM) {
                candleFlames[i].setIcon(dimCandle);
                System.out.println("Setting candle " + i + " to red");
            } else {
                candleFlames[i].setIcon(null);
                System.out.println("Setting candle " + i + " to snuff");
            }
        }
        System.out.println(feedback);
    }

    private void printPastGuesses(List<Guess> guesses) {
        for (int i = 0; i < guesses.size(); i++) {
            String guess = guesses.get(i).getGuessedCode();
            List<CandleType> feedback = guesses.get(i).getFeedback();
            for (int c = 0; c < guess.length(); c++) {
                String character = String.valueOf(guess.charAt(c));
                if (feedback.get(c) == CandleType.BRIGHT) {
                    attempts[i][c].setForeground(new Color(255, 117, 24));
                } else if (feedback.get(c) == CandleType.DIM) {
                    attempts[i][c].setForeground(new Color(179, 27, 27));
                } else {
                    attempts[i][c].setForeground(Color.DARK_GRAY);
                }
                attempts[i][c].setText(character);
            }
        }

    }

//    public static void main(String[] args) {
//        JFrame frame = new JFrame("Light it Up!");
//
//        CandleGameView view = new CandleGameView(null, null, null);
//
//        frame.setContentPane(view);
//        frame.setSize(800, 600);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setLocationRelativeTo(null);
//        frame.setVisible(true);
//    }

    public static void main(String[] args) {
        FontManager.loadFonts();
        SwingUtilities.invokeLater(() -> {

            // Temporary game for testing 12345
            // Temporary game 69875
            CandleGame game = new CandleGame("69875", 10);

            CandleGameViewModel viewModel = new CandleGameViewModel();
            EvaluateGuessPresenter presenter =
                    new EvaluateGuessPresenter(viewModel);
            EvaluateGuessInteractor interactor =
                    new EvaluateGuessInteractor(presenter);
            EvaluateGuessController controller =
                    new EvaluateGuessController(interactor);
            CandleGameView view =
                    new CandleGameView(controller, viewModel, game);

            JFrame frame = new JFrame("Light it Up!");
            frame.setContentPane(view);
            frame.setSize(800, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

}
