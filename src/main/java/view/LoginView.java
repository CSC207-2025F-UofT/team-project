package view;

import app.SignUpApp;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginViewModel;

import javax.swing.*;
import java.awt.GridLayout;

public class LoginView extends JDialog{
    public LoginView(LoginController controller,
                     LoginViewModel model) {

        setTitle("Login");
        setSize(400, 200);
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridLayout(3, 2, 5, 5));

        JTextField username = new JTextField();
        JPasswordField password = new JPasswordField();

        JButton login = new JButton("Login");
        JButton signUp = new JButton("Sign Up");

        add(new JLabel("Username:"));
        add(username);

        add(new JLabel("Password:"));
        add(password);

        add(login);
        add(signUp);

        login.addActionListener(e -> {

            controller.execute(
                    username.getText(),
                    new String(password.getPassword()));

            password.setText("");

            JOptionPane.showMessageDialog(
                    this, model.getMessage());

            if (model.isSuccessful()) {
                dispose();
            }
        });

        signUp.addActionListener(e -> SignUpApp.open());
    }
}
