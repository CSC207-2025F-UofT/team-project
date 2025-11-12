package app;

import javax.swing.JFrame;

/**
 * MainFrame is the main window of the application.
 * Clean Architecture version - content is set externally.
 */
public class MainFrame extends JFrame {
    
    public MainFrame() {
        setTitle("Portfolio Tracker - Clean Architecture");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null); // Center the window
    }
    
    /**
     * TODO: Add menu bar with File, Edit, View, Help menus
     * TODO: Add status bar at bottom
     * TODO: Add toolbar for common actions
     * TODO: Support multiple portfolio tabs/windows
     */
}
