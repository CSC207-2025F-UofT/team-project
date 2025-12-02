package view;

import javax.swing.*;
import java.awt.*;
import service.Backend;
import service.Frontend;
import view.menu.SideMenuPanel;

/**
 * MainDashboardView is the top-level panel used after login.
 * It contains:
 *   - A left-side menu (SideMenuPanel)
 *   - A right-side content panel that changes depending on menu selection
 *
 * This class allows switching ONLY the right-side content
 * while keeping the left menu fixed.
 */
public class MainDashboardView extends JPanel {

    // Panel on the right side — the content that changes
    private final JPanel contentPanel;

    /**
     * Constructs the dashboard view for a specific user role.
     *
     * @param frontend The main application window
     * @param backend  The backend service for data access
     * @param userType The role of the current user (admin/staff/user)
     */
    public MainDashboardView(Frontend frontend, Backend backend, String userType) {
        setLayout(new BorderLayout());

        // Right content panel setup
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);

        // Left panel: the side menu.
        SideMenuPanel menu = new SideMenuPanel(frontend, backend, this, userType);

        // Initial content selection
        JPanel initial;

        switch (userType) {
            case "admin":
                // Admin dashboard
                initial = new AdminPage(frontend, backend);
                break;

            case "staff":
                // Staff dashboard placeholder
                initial = centerLabel("Staff Dashboard Coming Soon");
                break;

            case "user":
                // User dashboard placeholder
                initial = centerLabel("User Dashboard Coming Soon");
                break;

            default:
                initial = centerLabel("Unknown User Type");
        }

        contentPanel.add(initial, BorderLayout.CENTER);

        // Add panels to layout
        // fixed left menu
        add(menu, BorderLayout.WEST);
        // variable right content
        add(contentPanel, BorderLayout.CENTER);
    }

    /**
     * Switch the content on the right side.
     * Called by SideMenuPanel when a menu item is clicked.
     *
     * @param panel The new content panel to display
     */
    public void setContent(JPanel panel) {
        // Remove previous content
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        // Refresh UI
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    /**
     * Helper for quick creation of centered-label content panels.
     *
     * @param text Displayed text
     * @return JPanel containing centered JLabel
     */
    private JPanel centerLabel(String text) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JLabel(text, SwingConstants.CENTER), BorderLayout.CENTER);
        return p;
    }
}
