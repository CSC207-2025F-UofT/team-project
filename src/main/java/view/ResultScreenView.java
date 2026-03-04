package view;

import interface_adapter.opengame.OpenGameController;
import interface_adapter.result.ResultController;
import interface_adapter.result.ResultViewModel;

import javax.swing.*;
import java.awt.*;

public class ResultScreenView extends JPanel {

    private final ResultController controller;
    private final ResultViewModel viewModel;
    private final OpenGameController openGameController;

    private final JLabel titleLabel;
    private final JLabel messageLabel;
    private final JButton newGameButton;
    private final JButton exitButton;

    public ResultScreenView(ResultController controller,
                            ResultViewModel viewModel,
                            OpenGameController openGameController) {

        this.controller = controller;
        this.viewModel = viewModel;
        this.openGameController = openGameController;

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        /* ---------------- HEADER BAR ---------------- */
        JPanel header = new JPanel();
        header.setBackground(new Color(30, 144, 255));
        header.setPreferredSize(new Dimension(0, 90));
        header.setLayout(new GridBagLayout());

        titleLabel = new JLabel(" Congratulations!");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        header.add(titleLabel);

        add(header, BorderLayout.NORTH);

        /* ---------------- CENTER CARD ---------------- */
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        cardPanel.setOpaque(false);

        JPanel messageCard = new JPanel();
        messageCard.setLayout(new BoxLayout(messageCard, BoxLayout.Y_AXIS));
        messageCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        messageCard.setBackground(Color.WHITE);

        messageLabel = new JLabel("Game Completed!", SwingConstants.CENTER);
        messageLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        messageCard.add(messageLabel);
        cardPanel.add(messageCard);

        cardPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        /* ---------------- BUTTON PANEL ---------------- */
        newGameButton = new JButton(" Start New Game");
        newGameButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        newGameButton.setBackground(new Color(46, 139, 87));
        newGameButton.setForeground(Color.WHITE);
        newGameButton.setFocusPainted(false);
        newGameButton.setPreferredSize(new Dimension(220, 55));

        exitButton = new JButton(" Exit Game");
        exitButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        exitButton.setBackground(new Color(178, 34, 34));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.setPreferredSize(new Dimension(220, 55));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(newGameButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        buttonPanel.add(exitButton);

        cardPanel.add(buttonPanel);

        add(cardPanel, BorderLayout.CENTER);

        /* ---------------- LISTENERS ---------------- */

        // When ResultViewModel changes → update message
        //viewModel.addListener(() -> messageLabel.setText(viewModel.getMessage()));

        // START NEW GAME — call controller or OpenGame logic
        newGameButton.addActionListener(e -> {
            System.out.println("Starting new game...");
            openGameController.startNewGame();
        });

        // EXIT GAME
        exitButton.addActionListener(e -> System.exit(0));
    }

    public String getViewName() {
        return "result_screen";
    }

    public JButton getNewGameButton() {
        return newGameButton;
    }

    public JButton getExitButton() {
        return exitButton;
    }
}
