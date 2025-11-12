package app;

import javax.swing.*;
import java.awt.*;

/**
 * MainFrame is the main window of the application.
 */
public class MainFrame extends JFrame {
    
    public MainFrame() {
        setTitle("Java Swing Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // Center the window
        
        initComponents();
    }
    
    /**
     * Initialize the components of the frame.
     */
    private void initComponents() {
        // Set layout
        setLayout(new BorderLayout());
        
        // Create a welcome label
        JLabel welcomeLabel = new JLabel("Welcome to Java Swing!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(welcomeLabel, BorderLayout.CENTER);
        
        // Create a button panel at the bottom
        JPanel buttonPanel = new JPanel();
        JButton exampleButton = new JButton("Click Me!");
        exampleButton.addActionListener(e -> 
            JOptionPane.showMessageDialog(this, 
                "Hello from Java Swing!", 
                "Message", 
                JOptionPane.INFORMATION_MESSAGE)
        );
        buttonPanel.add(exampleButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
