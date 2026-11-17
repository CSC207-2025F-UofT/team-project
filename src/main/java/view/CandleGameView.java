package view;

import interface_adapters.candle_game.CandleGameViewModelListener;
import javax.swing.*;
import interface_adapters.candle_game.CandleGameController;

import java.io.File;
import java.io.IOException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CandleGameView extends JPanel implements ActionListener {

    private final CandleGameController controller;

    private final JLabel attempts;
    private final JLabel background;
    private final JTextField[] numberBoxes;
    private final JButton quitButton;
    private final JButton instructionsButton;
    private final JButton submitButton;

    /**
     * Constructs the Candle Game View
     * Initializes all it's components, and connects the View to the Controller
     *
     * @param controller the Controller for CandleGame
     */
    public CandleGameView(CandleGameController controller) {
        this.controller = controller;
        this.setLayout(null);

        // Font
        Font winkle = null;
        try {
            winkle = Font.createFont(Font.TRUETYPE_FONT, new File("src/font/Winkle-Regular.ttf"));
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Attempt Tracker
        attempts = new JLabel("110");
        attempts.setBounds(700, 20, 500, 35);
        attempts.setFont(winkle.deriveFont(48f));
        this.add(attempts);


        // Quit Button
        quitButton = new JButton("QUIT");
        quitButton.setBounds(10, 10, 100, 50);
        quitButton.setFont(winkle.deriveFont(30f));
        quitButton.setBackground(Color.decode("#362514"));
        quitButton.setForeground(Color.WHITE);
        quitButton.addActionListener(this);
        this.add(quitButton);

        // Instructions Button
        instructionsButton = new JButton("?");
        instructionsButton.setBounds(120, 10, 50, 50);
        instructionsButton.setFont(winkle.deriveFont(30f));
        instructionsButton.setBackground(Color.decode("#362514"));
        instructionsButton.setForeground(Color.WHITE);
        instructionsButton.addActionListener(this);
        this.add(instructionsButton);

        // Submit Button
        submitButton = new JButton("SUBMIT");
        submitButton.setBounds(340, 460, 120, 50);
        submitButton.setFont(winkle.deriveFont(30f));
        submitButton.setBackground(Color.decode("#362514"));
        submitButton.setForeground(Color.WHITE);
        submitButton.addActionListener(this);
        this.add(submitButton);

        // Background
        background = new JLabel(new ImageIcon("src/images/CANDLE_GAME_BG.png"));
        background.setBounds(0,0,800, 600);
        this.add(background);
        // Make sure it is the bottom Layer
        this.setComponentZOrder(background, this.getComponentCount() - 1);

        // Number Boxes
        numberBoxes = new JTextField[5];
        for (int i = 0; i < numberBoxes.length; i++) {
            numberBoxes[i] = new JTextField();
            numberBoxes[i].setBounds(70 + (i*150), 370, 60,30);
            numberBoxes[i].setFont(winkle.deriveFont(30f));
            numberBoxes[i].setHorizontalAlignment(JTextField.CENTER);
            this.add(numberBoxes[i]);
        }

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
            System.out.println("Submit button clicked!");
        }
    }


    private void instructionsOpen() {
        JFrame instructionsFrame = new JFrame("Instructions for Light it Up!");
        instructionsFrame.setMinimumSize(new java.awt.Dimension(400, 300));

        Font winkle;
        try {
            winkle = Font.createFont(Font.TRUETYPE_FONT, new File("src/font/Winkle-Regular.ttf"));
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }

        JLabel instructionsTitle = new JLabel("How to Play Light it Up");
        JLabel instructions = new JLabel("These are where the instructions will go");
        instructionsTitle.setFont(winkle.deriveFont(20f));
        instructions.setFont(winkle.deriveFont(10f));
        instructionsTitle.setBounds(100, 10, 200, 20);
        instructions.setBounds(10, 0, 100, 20);

        instructionsFrame.add(instructionsTitle);
        instructionsFrame.add(instructions);
        instructionsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        instructionsFrame.setLocationRelativeTo(this);
        instructionsFrame.setVisible(true);
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("Light it Up!");

        CandleGameView view = new CandleGameView(null);

        frame.setContentPane(view);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
