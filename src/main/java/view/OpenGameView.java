package view;

import interface_adapter.opengame.OpenGameController;
import interface_adapter.opengame.OpenGameViewModel;

import javax.swing.*;
import java.awt.*;

public class OpenGameView extends JPanel {
    // TODO: Add a view name
    private final OpenGameController controller;
    private final OpenGameViewModel viewModel;

    private final JTextField startField = new JTextField(15);
    private final JTextField destinationField = new JTextField(15);

    private final JButton newGameButton = new JButton("Start New Game");
    private final JButton continueGameButton = new JButton("Continue Game");
    private final JLabel messageLabel = new JLabel("Welcome!");
    // TODO: Change the signature, only the view model is the input
    public OpenGameView(OpenGameController controller,
                        OpenGameViewModel viewModel) {

        this.controller = controller;
        this.viewModel = viewModel;

        // TODO: change the add listener stuff
        // View listens to ViewModel updates
        this.viewModel.addListener(this::updateView);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(300, 250));

        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createRigidArea(new Dimension(0, 10)));
        add(messageLabel);

        // Starting Location
        add(new JLabel("Starting Location:"));
        add(startField);

        // Destination
        add(new JLabel("Destination:"));
        add(destinationField);

        newGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        continueGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        newGameButton.addActionListener(e -> {
            String start = startField.getText();
            String dest = destinationField.getText();
            controller.startNewGame(start, dest);
        });

        continueGameButton.addActionListener(e -> controller.continueGame());

        add(Box.createRigidArea(new Dimension(0, 10)));
        add(newGameButton);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(continueGameButton);
    }

    private void updateView() {
        messageLabel.setText(viewModel.getMessage());
    }
}
