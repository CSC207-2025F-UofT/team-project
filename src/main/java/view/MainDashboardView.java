package view;

import javax.swing.*;
import java.awt.*;
import service.Backend;
import service.Frontend;

public class MainDashboardView extends JPanel {

    public MainDashboardView(Frontend frontend, Backend backend, String userType) {
        setLayout(new BorderLayout());

        // left menu
        SideMenuPanel menu = new SideMenuPanel(frontend, backend, userType);

        // middle
        JPanel content;

        switch (userType) {

            case "admin":
                content = new AdminPage(frontend, backend);   // ★ 正确传 frontend
                System.out.println(">> Loading AdminPage");
                break;

            case "staff":
                content = new JPanel(new BorderLayout());
                content.add(new JLabel("Staff Dashboard Coming Soon",
                        SwingConstants.CENTER), BorderLayout.CENTER);
                break;

            case "user":
                content = new JPanel(new BorderLayout());
                content.add(new JLabel("User Dashboard Coming Soon",
                        SwingConstants.CENTER), BorderLayout.CENTER);
                break;

            default:
                content = new JPanel(new BorderLayout());
                content.add(new JLabel("Unknown User Type",
                        SwingConstants.CENTER), BorderLayout.CENTER);
        }

        add(menu, BorderLayout.WEST);
        add(content, BorderLayout.CENTER);
    }
}
