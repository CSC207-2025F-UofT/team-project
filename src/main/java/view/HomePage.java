package view;
import interface_adapter.OpenNewGame.NewGameController;

import javax.swing.*;
import java.awt.*;

import interface_adapter.opengame.OpenGameController;

public class HomePage {

    private final NewGameController controller;
    JFrame frame = new JFrame("HomePage");
    JButton StartButton = new JButton("Start Game");
    JButton ContinueButton = new JButton("Continue Game");
    JButton QuitButton = new JButton("Quit");
    JTextField UserNameField = new JTextField(20);

    public HomePage(NewGameController controller)
    {
        this.controller = controller;
        frame.setMinimumSize(new java.awt.Dimension(300, 200));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Doing Closing for now
        frame.setResizable(true);

        JPanel UserNamePanel = new JPanel();
        UserNamePanel.add(new JLabel("Username:"));
        UserNamePanel.add(UserNameField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(StartButton);
        buttonPanel.add(ContinueButton);
        buttonPanel.add(QuitButton);

        StartButton.addActionListener(e -> {
            String first = UserNameField.getText();
            controller.StartNewGame(first);
        });
//        ContinueButton.addActionListener(e -> {
//            UserNameField.setText("");
//        });
        QuitButton.addActionListener(e -> {
            UserNameField.setText("");
        });

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(UserNamePanel);
        mainPanel.add(buttonPanel);

        // 8. Add to frame
        frame.setContentPane(mainPanel);
        frame.pack();
        frame.setVisible(true);
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(HomePage::new);
//    }

}
