package com.studyarc.use_case.track_plan.TestOnUI;

import com.studyarc.view.TrackPlansView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TestLoginView extends JPanel {
    JButton login = new JButton("Login");
    CardLayout cardLayout;
    JPanel parent;


    public TestLoginView(JPanel parent, CardLayout cardLayout) {
        this.parent = parent;
        this.cardLayout = cardLayout;

        viewsetup();

        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(parent, "loggedin");
            }
        });
    }

    private void viewsetup() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel username = new JLabel("Username:");
        JTextField usernameField = new JTextField(10);

        JLabel password = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(10);

        // Username row (center)
        JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        usernamePanel.add(username);
        usernamePanel.add(usernameField);

        // Password row (center)
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        passwordPanel.add(password);
        passwordPanel.add(passwordField);

        // Login button row (center)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(login);

        this.add(usernamePanel);
        this.add(passwordPanel);
        this.add(buttonPanel);
    }


}
