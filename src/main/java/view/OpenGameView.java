package view;

import interface_adapter.opengame.OpenGameController;
import interface_adapter.opengame.OpenGameViewModel;

import javax.swing.*;
import java.awt.*;

public class OpenGameView extends JPanel {

    private final String viewName = "OpenGame";

    private OpenGameController controller;
    private final OpenGameViewModel viewModel;

    private final JButton newGameButton = new JButton("Start New Game");
    private final JButton continueGameButton = new JButton("Continue Game");
    private final JLabel messageLabel = new JLabel("Welcome!", SwingConstants.CENTER);

    public OpenGameView(OpenGameViewModel viewModel) {
        this.controller = null;
        this.viewModel = viewModel;

        // Listen to ViewModel updates
        this.viewModel.addListener(this::updateView);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setPreferredSize(new Dimension(1000, 200));

        // Center align
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        newGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        continueGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Buttons
//        newGameButton.addActionListener(e -> controller.startNewGame());
        newGameButton.addActionListener(e -> onStartNewGame());
        continueGameButton.addActionListener(e -> controller.continueGame());

        add(Box.createRigidArea(new Dimension(0, 20)));
        add(messageLabel);
        add(Box.createRigidArea(new Dimension(0, 20)));

        add(newGameButton);
        add(Box.createRigidArea(new Dimension(0, 10)));

        add(continueGameButton);
    }
    private void onStartNewGame() {
        controller.startNewGame();
    }

    private void updateView() {
        messageLabel.setText(viewModel.getMessage());
    }

    public String getViewName() {
        return viewName;
    }

    public void setOpenGameController(OpenGameController openGameController) {
        this.controller = openGameController;
    }
}
