package view;

import javax.swing.*;

public class MainMenu extends JFrame {

    public MainMenu() {
        setTitle("Pet Adoption System");
        setSize(300, 200);
        setLayout(null);

        JButton registerBtn = new JButton("create new account");
        JButton userLoginBtn = new JButton("user login");
        JButton adminLoginBtn = new JButton("admin login");

        registerBtn.setBounds(50, 30, 180, 30);
        userLoginBtn.setBounds(50, 70, 180, 30);
        adminLoginBtn.setBounds(50, 110, 180, 30);

        add(registerBtn);
        add(userLoginBtn);
        add(adminLoginBtn);

//        registerBtn.addActionListener(e -> new UserRegister());
//        userLoginBtn.addActionListener(e -> new UserLogin());
//        adminLoginBtn.addActionListener(e -> new AdminLogin());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
