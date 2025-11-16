package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.logout.LogoutController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The View for displaying Account Details and handling the Logout functionality.
 */
public class AccountDetailsView extends JPanel implements ActionListener, PropertyChangeListener {

    public final String viewName = "account details";
    private final ViewManagerModel viewManagerModel;
    private LogoutController logoutController; // Will need a setter for this

    // Components
    private final JButton logoutButton;
    private final JLabel additionalInfoLabel;

    // Top bar component for the title and navigation
    private final JPanel topBar;

    public AccountDetailsView(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top Bar (Title and Back Button)
        topBar = new JPanel(new BorderLayout());

        // Left Side: Back/Exit Button (Go back to LoggedInView/Recent Chats)
        JButton backButton = new JButton("⬅"); // Unicode for back arrow
        backButton.setFont(new Font("Oxygen", Font.BOLD, 20));
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);

        backButton.addActionListener(e -> {
            // Navigate back to the LoggedInView
            viewManagerModel.setState("logged in");
            viewManagerModel.firePropertyChange();
        });

        topBar.add(backButton, BorderLayout.WEST);

        // Center: Title
        JLabel title = new JLabel("Account Details", SwingConstants.CENTER);
        title.setFont(new Font("Oxygen", Font.BOLD, 24));
        topBar.add(title, BorderLayout.CENTER);

        // Main Content Area
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));

        additionalInfoLabel = new JLabel("Additional Information here");
        additionalInfoLabel.setFont(new Font("Oxygen", Font.ITALIC, 16));
        additionalInfoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Placeholder for real account data (username, email, etc.)
        JLabel usernamePlaceholder = new JLabel("Username: [Current User]");
        usernamePlaceholder.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel statusPlaceholder = new JLabel("Status: Active");
        statusPlaceholder.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(usernamePlaceholder);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(statusPlaceholder);
        contentPanel.add(Box.createVerticalStrut(30));
        contentPanel.add(additionalInfoLabel);
        contentPanel.add(Box.createVerticalGlue()); // Push elements up

        // Logout Button Panel (Bottom)
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(this);

        southPanel.add(logoutButton);

        // Assembly
        this.add(topBar, BorderLayout.NORTH);
        this.add(contentPanel, BorderLayout.CENTER);
        this.add(southPanel, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource().equals(logoutButton) && logoutController != null) {
            // Use the existing Logout use case
            logoutController.execute();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // Future logic for updating username/details here
    }

    public String getViewName() {
        return viewName;
    }

    public void setLogoutController(LogoutController logoutController) {
        this.logoutController = logoutController;
    }
}