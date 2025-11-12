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

    /* new parameter Runnable onLoginSuccess */
    public LoginView(LoginController loginController, Runnable showSignUpView, Runnable onLoginSuccess) {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 180);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3 ,2, 6, 6)); //spaceing change on hgap and vgap
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
            
            if (output.isSuccess()) {
                // Close login window first, then show dashboard
                dispose();
                onLoginSuccess.run();  // go to dashboard
            } else {
                // Only show error dialog for failures
                JOptionPane.showMessageDialog(this, output.getMessage(), "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        signUpButton.addActionListener(e -> {
            showSignUpView.run();
            dispose();
        });

    }
}
