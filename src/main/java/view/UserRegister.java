package view;

//import repository.UserRepository;

import javax.swing.*;
import java.awt.*;

public class UserRegister extends JFrame {

    public UserRegister() {
        setTitle("user login");
        setSize(400, 300);
        setLayout(new GridLayout(5, 2));

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        JButton registerBtn = new JButton("login");
        JButton cancelBtn = new JButton("cancel");

        add(new JLabel("username："));
        add(usernameField);
        add(new JLabel("password："));
        add(passwordField);

        add(registerBtn);
        add(cancelBtn);

        registerBtn.addActionListener(e -> {
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());
        });

        cancelBtn.addActionListener(e -> dispose());

        setVisible(true);
    }
}
