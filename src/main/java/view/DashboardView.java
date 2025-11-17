package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DashboardView extends JPanel {

    private final JFrame frame;
    private JPanel centerPanel;  // Where all content pages will load

    public DashboardView(JFrame frame) {
        this.frame = frame;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        // Colors
        Color bgBlack = Color.decode("#000000");
        Color panelDark = Color.decode("#020F28");
        Color accentHover = Color.decode("#0047A3");
        Color textLight = Color.decode("#E6E6E6");
        Color buttonBase = panelDark;

        // Header (top)
        JLabel header = new JLabel("LockIn!", SwingConstants.CENTER);
        header.setFont(new Font("Georgia", Font.BOLD, 32));
        header.setOpaque(true);
        header.setBackground(panelDark);
        header.setForeground(textLight);
        header.setPreferredSize(new Dimension(0, 80));
        add(header, BorderLayout.NORTH);

        // Sidebar (left)
        JPanel sidebar = new JPanel(new GridLayout(6,1,0,0));
        sidebar.setBackground(bgBlack);
        sidebar.setPreferredSize(new Dimension(220, 0));

        String[] buttons = {"Home", "Grades", "Calendar", "Task Manager", "Courses", "Logout"};
        for (String text : buttons) {
            JButton btn = new JButton(text);
            btn.setFont(new Font("Georgia", Font.PLAIN, 18));
            btn.setForeground(textLight);
            btn.setBackground(buttonBase);
            btn.setOpaque(true);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            // Hover effect
            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    btn.setBackground(accentHover);
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    btn.setBackground(buttonBase);
                }
            });

            // Action listener for page switching
            btn.addActionListener(e -> {
                switch (text) {
                    case "Home":
                        showHomePanel(); // Switches to the Home content
                        break;
                    case "Task Manager":
                        showTasksPanel(); // Switches to the TasksPanel content
                        break;
                    case "Logout":
                        // Placeholder for actual logout logic
                        System.exit(0);
                        break;
                    // Add logic for Grades, Calendar, and Courses here...
                }
            });

            // Wrapper panel to handle margin/padding around the button
            JPanel wrapper = new JPanel(new BorderLayout());
            wrapper.setOpaque(false);
            wrapper.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            wrapper.add(btn, BorderLayout.CENTER);
            sidebar.add(wrapper);
        }

        add(sidebar, BorderLayout.WEST);

        // Center content panel
        centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(bgBlack);
        add(centerPanel, BorderLayout.CENTER);

        // Show the initial panel (Home) when the dashboard loads
        showHomePanel();
    }

    // --- PAGE SWITCHING METHODS ---

    /**
     * Replaces the content of centerPanel with a new TasksPanel.
     */
    private void showTasksPanel() {
        centerPanel.removeAll();
        // Uses the TasksPanel class provided earlier
        centerPanel.add(new TasksPanel(), BorderLayout.CENTER);
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    /**
     * Replaces the content of centerPanel with the Home Panel.
     * Uses the simple placeholder HomePanel class.
     */
    private void showHomePanel() {
        centerPanel.removeAll();
        // Uses the separate HomePanel class
        centerPanel.add(new HomePanel(), BorderLayout.CENTER);
        centerPanel.revalidate();
        centerPanel.repaint();
    }
}
