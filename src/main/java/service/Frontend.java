package service;

import view.AdminPage;

import javax.swing.*;
import java.awt.event.*;

public class Frontend extends JFrame {

    private final Backend backend;

    public Frontend(Backend backend) {
        this.backend = backend;

        setTitle("Pet Adoption Service");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                backend.toJsonFiles("users.json", "pets.json", "admins.json", "applications.json");
                dispose();                 // closes the frame
                System.exit(0);    // exits the application (optional)
            }
        });

        showAdminPage();
    }

    public void showAdminPage() {
        setContentPane(new AdminPage(this, backend));
        revalidate();
        repaint();
    }
}
