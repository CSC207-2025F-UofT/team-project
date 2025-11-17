package view;

import interface_adapter.logged_in.ChangePasswordController;
import interface_adapter.logout.LogoutController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DashboardView extends JPanel {

    // --- Card Identifiers ---
    private static final String HOME_CARD = "Dashboard";
    private static final String TASK_MANAGER_CARD = "Task Manager";
    private static final String CALENDAR_CARD = "Calendar";

    // --- State Fields ---
    private final JFrame frame;
    private final CardLayout cardLayout;
    private final JPanel cardPanel; // The panel that holds the different content views

    private final JLabel mainHeaderLabel; // NEW: Header label to change text dynamically

    private JButton currentSelectedButton = null; // NEW: Holds the currently selected sidebar button

    // Sub-Panel Instances
    private final HomePanel homePanel;
    private final TasksPanel tasksPanel;
    private final CalendarPanel calendarPanel;

    // Controllers
    private ChangePasswordController changePasswordController = null;
    private LogoutController logoutController = null;

    // --- Color Palette ---
    private final Color BG_BLACK = Color.decode("#000000");
    private final Color PANEL_DARK = Color.decode("#020F28");
    private final Color ACCENT_HOVER = Color.decode("#0047A3");
    private final Color TEXT_LIGHT = Color.decode("#E6E6E6");
    private final Color BUTTON_BASE = PANEL_DARK;

    public DashboardView(JFrame frame) {
        this.frame = frame;
        this.cardLayout = new CardLayout();
        this.cardPanel = new JPanel(cardLayout);

        // Initialize all sub-panels once
        this.homePanel = new HomePanel();
        this.tasksPanel = new TasksPanel();
        this.calendarPanel = new CalendarPanel();

        // Initialize the new header label
        this.mainHeaderLabel = new JLabel("LockIn Dashboard", SwingConstants.CENTER);

        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        // --- Global Header (North) ---
        mainHeaderLabel.setFont(new Font("Georgia", Font.BOLD, 32));
        mainHeaderLabel.setOpaque(true);
        mainHeaderLabel.setBackground(PANEL_DARK);
        mainHeaderLabel.setForeground(TEXT_LIGHT);
        mainHeaderLabel.setPreferredSize(new Dimension(0, 80));
        add(mainHeaderLabel, BorderLayout.NORTH);

        // --- Main Content Panel (Center) with CardLayout ---
        cardPanel.setBackground(BG_BLACK);

        // Add all views to the CardPanel
        cardPanel.add(homePanel, HOME_CARD);
        cardPanel.add(tasksPanel, TASK_MANAGER_CARD);
        cardPanel.add(calendarPanel, CALENDAR_CARD);

        add(cardPanel, BorderLayout.CENTER);


        // --- Sidebar (West) ---
        JPanel sidebar = new JPanel();
        // Updated layout to only include the four buttons from your design
        sidebar.setLayout(new GridLayout(4, 1, 0, 0));
        sidebar.setBackground(BG_BLACK);
        sidebar.setPreferredSize(new Dimension(220, 0));

        String[] buttons = {HOME_CARD, CALENDAR_CARD, TASK_MANAGER_CARD, "Logout"};

        for (String text : buttons) {
            JButton btn = new JButton(text);
            btn.setFont(new Font("Georgia", Font.PLAIN, 18));
            btn.setForeground(TEXT_LIGHT);
            btn.setBackground(BUTTON_BASE);
            btn.setOpaque(true);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            // Set initial selection for the Dashboard button
            if (text.equals(HOME_CARD)) {
                btn.setBackground(ACCENT_HOVER);
                currentSelectedButton = btn;
            }

            // --- Mouse Listener for Hover Effect ---
            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (btn != currentSelectedButton) {
                        btn.setBackground(ACCENT_HOVER.brighter());
                    }
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    if (btn != currentSelectedButton) {
                        btn.setBackground(BUTTON_BASE);
                    }
                }
            });

            // --- Action Listener for Selection and Navigation ---
            btn.addActionListener(e -> {
                // 1. Reset previous selection and set new selection
                if (currentSelectedButton != null) {
                    currentSelectedButton.setBackground(BUTTON_BASE);
                }
                currentSelectedButton = btn;
                btn.setBackground(ACCENT_HOVER); // Solid highlight for selected tab

                // 2. Navigate and UPDATE GLOBAL HEADER TEXT
                switch (text) {
                    case HOME_CARD:
                        cardLayout.show(cardPanel, HOME_CARD);
                        mainHeaderLabel.setText("LockIn Dashboard");
                        break;
                    case TASK_MANAGER_CARD:
                        cardLayout.show(cardPanel, TASK_MANAGER_CARD);
                        mainHeaderLabel.setText(TASK_MANAGER_CARD);
                        break;
                    case CALENDAR_CARD:
                        cardLayout.show(cardPanel, CALENDAR_CARD);
                        mainHeaderLabel.setText(CALENDAR_CARD);
                        break;
                    case "Logout":
                        // Execute Logout use case if controller is set
                        if (logoutController != null) {
                            logoutController.execute();
                        } else {
                            // If controller is null, we restore the button state
                            currentSelectedButton.setBackground(BUTTON_BASE);
                            currentSelectedButton = null;
                            JOptionPane.showMessageDialog(frame, "LogoutController not set.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        break;
                }
            });

            JPanel buttonWrapper = new JPanel(new BorderLayout());
            buttonWrapper.setOpaque(false);
            buttonWrapper.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            buttonWrapper.add(btn, BorderLayout.CENTER);
            sidebar.add(buttonWrapper);
        }
        add(sidebar, BorderLayout.WEST);

        // Set initial view
        cardLayout.show(cardPanel, HOME_CARD);

        // Set the main panel background
        this.setBackground(BG_BLACK);
    }


    // --- PUBLIC SETTERS (Required for LoggedInView integration) ---

    /**
     * Called by LoggedInView to update the welcome message on the Home Panel.
     * @param username The username of the currently logged-in user.
     */
    public void updateUsername(String username) {
        this.homePanel.setUsername(username);
    }

    public void setChangePasswordController(ChangePasswordController changePasswordController) {
        this.changePasswordController = changePasswordController;
        // Connect this controller to a button/dialog if one exists in the Dashboard
    }

    public void setLogoutController(LogoutController logoutController) {
        this.logoutController = logoutController;
    }
}
