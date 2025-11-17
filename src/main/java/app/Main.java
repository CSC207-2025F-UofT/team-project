package app;

import view.DashboardView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("LockIn!");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);

            DashboardView dashboardView = new DashboardView(frame);

            // Important: use BorderLayout so left panel stays visible
            frame.getContentPane().setLayout(new java.awt.BorderLayout());
            frame.getContentPane().add(dashboardView, java.awt.BorderLayout.CENTER);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
