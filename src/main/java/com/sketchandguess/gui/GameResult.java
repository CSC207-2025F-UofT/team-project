package com.sketchandguess.gui;

import javax.swing.*;
import java.awt.*;
import javax.swing.JLabel;

public class GameResult extends JPanel {

    private final Application app;   // to switch screens
    private final JLabel promptLabel;
    private final JLabel aiGuessLabel;
    private final JLabel timeTakenLabel;
    private final JLabel reasonLabel;
    private final JLabel resultMessage;

    public GameResult(Application app) {
        this.app = app;
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Game Result", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
        add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(4, 1, 0, 8));

        promptLabel = new JLabel("Prompt: ");
        aiGuessLabel = new JLabel("AI Guess: ");
        timeTakenLabel = new JLabel("Time Taken: ");
        reasonLabel = new JLabel("Reason: "); // “Time ran out” / “AI guess wrong”

        centerPanel.add(promptLabel);
        centerPanel.add(aiGuessLabel);
        centerPanel.add(timeTakenLabel);
        centerPanel.add(reasonLabel);

        add(centerPanel, BorderLayout.CENTER);

        resultMessage = new JLabel("You win! / You lose!", SwingConstants.CENTER);
        add(resultMessage, BorderLayout.SOUTH);

        JPanel bottomPanel = new JPanel();
        JButton retryButton = new JButton("Retry");
        retryButton.addActionListener(e -> app.showGame());
        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> app.showMainmenu());
        bottomPanel.add(retryButton);
        bottomPanel.add(backButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }
}

//Todo: use controller to get the data about prompt, time taken....