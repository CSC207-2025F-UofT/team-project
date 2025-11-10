package ui;

import auth.interface_adapters.controllers.LoginController;
import auth.use_case.login.LoginOutputData;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class LoginView extends JFrame {

    private final JTextField usernameField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JButton loginButton = new JButton("Login");
    private final JButton signUpButton = new JButton("Sign Up");

    public LoginView(LoginController loginController, Runnable showSignUpView) {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 180);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3 ,2, 5, 5));
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(signUpButton);
        add(panel);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = Arrays.toString(passwordField.getPassword());

            LoginOutputData output = loginController.login(username, password);
            JOptionPane.showMessageDialog(this, output.getMessage());
            if (output.isSuccess()) {
                // insert code for dashboard here
                System.out.println("Login success — navigate to dashboard!");
            }
        });

        signUpButton.addActionListener(e -> {
            showSignUpView.run();
            dispose();
        });

    }
}
