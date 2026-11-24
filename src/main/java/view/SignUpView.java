package view;

import interface_adapter.signup.SignUpController;
import interface_adapter.signup.SignUpViewModel;

import javax.swing.*;
import java.awt.*;

public class SignUpView extends JDialog {

    public SignUpView(SignUpController controller,
                      SignUpViewModel model) {

        setTitle("Sign Up");
        setSize(400, 230);
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridLayout(5, 2, 5, 5));

        JTextField username = new JTextField();
        JTextField email = new JTextField();
        JPasswordField password = new JPasswordField();
        JPasswordField repeatPassword = new JPasswordField();

        JButton signUp = new JButton("Sign Up");
        JButton close = new JButton("Close");

        add(new JLabel("Username:"));
        add(username);

        add(new JLabel("Email:"));
        add(email);

        add(new JLabel("Password:"));
        add(password);

        add(new JLabel("Repeat Password:"));
        add(repeatPassword);

        add(signUp);
        add(close);

        signUp.addActionListener(e -> {
            controller.execute(
                    username.getText(),
                    email.getText(),
                    new String(password.getPassword()),
                    new String(repeatPassword.getPassword())
            );

            JOptionPane.showMessageDialog(
                    this, model.getMessage());

            if (model.isSuccessful()) {
                dispose();
            }
        });

        close.addActionListener(e -> dispose());
    }
}
