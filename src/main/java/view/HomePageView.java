package view;

import javax.swing.*;

public class HomePageView {

    public HomePageView() {
        SwingUtilities.invokeLater(() -> {

            //** WELCOME PANEL **//
            JPanel welcomePanel = new JPanel();
            JLabel welcomeLabelText = new JLabel("Welcome");
            welcomePanel.add(welcomeLabelText);

            //** Sign In Button **//
            JPanel signInButtonPanel = new JPanel();
            JButton signInSubmit = new JButton("Sign In");
            signInButtonPanel.add(signInSubmit);

            //** Sign In Button Event **//
            signInSubmit.addActionListener(e -> {
                // Call the controller.
            });

            //** Create Account Button **//
            JPanel createAccountButtonPanel = new JPanel();
            JButton createAccountSubmit = new JButton("Create Account");
            createAccountButtonPanel.add(createAccountSubmit);

            //** Create Account Button Event **//
            createAccountSubmit.addActionListener(e -> {
                // Call the controller.
            });


            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(welcomePanel);
            mainPanel.add(signInButtonPanel);
            mainPanel.add(createAccountButtonPanel);

            JFrame frame = new JFrame("Sophisticated Weather Application");
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });
    }
}
