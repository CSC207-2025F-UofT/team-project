package view;

import javax.swing.*;
import java.awt.*;

public class SignUpView extends JFrame {

  private JTextField usernameField;
  private JPasswordField passwordField;
  private JPasswordField confirmpasswordField;
  private JButton signUpButton;
  private JButton backToLogInButton;

  public SignUpView() {
    setTitle("Sign up");
    setSize(400, 150);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(3, 2, 5, 5));
    usernameField = new JTextField();
    passwordField = new JPasswordField();
    confirmpasswordField = new JPasswordField();
    signUpButton = new JButton("Sign up");
    backToLogInButton = new JButton("Back to Log in");
    panel.add(new JLabel("Set a Username:"));
    panel.add(usernameField);
    panel.add(new JLabel("Set a Password"));
    panel.add(passwordField);
    panel.add(new JLabel("Repeat the Password"));
    panel.add(confirmpasswordField);
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout(1, 2, 10, 10));
    buttonPanel.add(signUpButton);
    buttonPanel.add(backToLogInButton);
    add(panel, BorderLayout.CENTER);
    add(buttonPanel, BorderLayout.SOUTH);
  }

  public static void main(String[] args) {
    new SignUpView().setVisible(true);
  }
}

