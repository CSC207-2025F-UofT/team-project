package view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateAccountView {
    public CreateAccountView() {
        SwingUtilities.invokeLater(() -> {

            //** Username Panel **//
            JPanel userNamePanel = new JPanel();
            JTextField userNameField = new JTextField(30);
            userNamePanel.add(new JLabel("Email:"));
            userNamePanel.add(userNameField);

            //** Email Panel **//
            JPanel emailPanel = new JPanel();
            JTextField emailField = new JTextField(30);
            emailPanel.add(new JLabel("Email:"));
            emailPanel.add(emailField);

            //** Password Panel **//
            JPanel passwordPanel = new JPanel();
            JTextField passwordField = new JTextField(30);
            passwordPanel.add(new JLabel("Password:"));
            passwordPanel.add(passwordField);

            //** Confirm Password Panel **//
            JPanel confirmPasswordPanel = new JPanel();
            JTextField confirmPasswordField = new JTextField(30);
            confirmPasswordPanel.add(new JLabel("Confirm Password:"));
            confirmPasswordPanel.add(confirmPasswordField);

            //** Submit Button Panel **//
            JPanel submitButtonPanel = new JPanel();
            JButton submit = new JButton("Sign In");
            submitButtonPanel.add(submit);

            //** Submit Button Event **//
            submit.addActionListener(e -> {
                String email = emailField.getText();
                String password = passwordField.getText();
                String confirmPassword = confirmPasswordField.getText();

                // Call the controller.

            });

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(userNamePanel);
            mainPanel.add(emailPanel);
            mainPanel.add(passwordPanel);
            mainPanel.add(confirmPasswordPanel);
            mainPanel.add(submit);

            JFrame frame = new JFrame("Sophisticated Weather Application");
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });
    }
}
