package view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignInView {
    public SignInView() {
        SwingUtilities.invokeLater(() -> {

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

            //** Submit Button Panel **//
            JPanel submitButtonPanel = new JPanel();
            JButton submit = new JButton("Sign In");
            submitButtonPanel.add(submit);

            //** Submit Button Event **//
            submit.addActionListener(e -> {
                String email = emailField.getText();;
                String password = passwordField.getText();

                // Call the controller.

            });

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(emailPanel);
            mainPanel.add(passwordPanel);
            mainPanel.add(submit);

            JFrame frame = new JFrame("Sophisticated Weather Application");
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });
    }
}
