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

            btn.addActionListener(e -> {
                if (text.equals("Task Manager")) {
                    showTasksPanel();
                }
                if (text.equals("Calendar")) {
                    showCalendarPanel();
                }
            });

            JPanel wrapper = new JPanel(new BorderLayout());
            wrapper.setOpaque(false);
            wrapper.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            wrapper.add(btn, BorderLayout.CENTER);
            sidebar.add(wrapper);
        }

        add(sidebar, BorderLayout.WEST);

        // Center content panel (empty on start)
        centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(bgBlack);
        add(centerPanel, BorderLayout.CENTER);
    }

    // PAGE SWITCHING — replaces only the centerPanel
    private void showTasksPanel() {
        centerPanel.removeAll();
        centerPanel.add(new TasksPanel(), BorderLayout.CENTER);
        centerPanel.revalidate();
        centerPanel.repaint();
    }
    private void showCalendarPanel() {
        centerPanel.removeAll();
        centerPanel.add(new CalendarPanel(), BorderLayout.CENTER);
        centerPanel.revalidate();
        centerPanel.repaint();
    }
}
