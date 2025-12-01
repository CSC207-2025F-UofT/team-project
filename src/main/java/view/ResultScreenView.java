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

    private final JLabel messageLabel = new JLabel("", SwingConstants.CENTER);
    private final JButton newGameButton = new JButton("Start New Game");
    private final JButton exitButton = new JButton("Exit Game");
    // TODO: Change the signature, only the view model is the input
    public ResultScreenView(ResultController controller,
                            ResultViewModel viewModel,
                            OpenGameController openGameController) {

        this.controller = controller;
        this.viewModel = viewModel;
        this.openGameController = openGameController;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(350, 250));

        messageLabel.setFont(new Font("Arial", Font.BOLD, 20));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        newGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createRigidArea(new Dimension(0, 40)));
        add(messageLabel);
        add(Box.createRigidArea(new Dimension(0, 30)));
        add(newGameButton);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(exitButton);

        // Listeners
        viewModel.addListener(() -> messageLabel.setText(viewModel.getMessage()));

        newGameButton.addActionListener(e -> openGameController.startNewGame("A", "Z"));
        exitButton.addActionListener(e -> System.exit(0));
    }
}
