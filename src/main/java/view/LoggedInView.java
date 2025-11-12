package view;

import interface_adapter.logged_in.ChangePasswordController;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.logout.LogoutController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The View for when the user is logged into the program, now displaying the chat list.
 */
public class LoggedInView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "logged in";
    private final LoggedInViewModel loggedInViewModel;
    private ChangePasswordController changePasswordController = null;
    private LogoutController logoutController;

    // Components for the New Design
    private final JLabel usernameLabel; // To display the logged-in username in the top bar
    private final JList<String> recentChatsList; // Component to hold the list of chats
    private final JPanel recentPanel; // Panel containing "Recent Chats" text

    // NEW BUTTONS
    private final JButton profileButton; // User icon button
    private final JButton newChatButton; // Plus icon button


    private final JButton logOut;
    private final JButton changePassword;

    public LoggedInView(LoggedInViewModel loggedInViewModel) {
        this.loggedInViewModel = loggedInViewModel;
        this.loggedInViewModel.addPropertyChangeListener(this);

        // Top Bar Panel (User, New Chat)
        JPanel topBar = new JPanel(new BorderLayout(5, 0));
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

        // Left Side: Profile Button and Username Label
        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));

        // Profile Button (User Icon)
        profileButton = new JButton("👤");
        profileButton.setFont(new Font("SansSerif", Font.PLAIN, 24));
        profileButton.setFocusPainted(false);
        profileButton.setBorderPainted(false);
        profileButton.setContentAreaFilled(false);

        usernameLabel = new JLabel("User"); // Placeholder text, updated on login
        usernameLabel.setFont(new Font("SansSerif", Font.BOLD, 18));

        userInfoPanel.add(profileButton);
        userInfoPanel.add(usernameLabel);

        topBar.add(userInfoPanel, BorderLayout.WEST);

        // Right Side: New Chat Button (Plus Icon)
        newChatButton = new JButton("⊕");
        newChatButton.setFont(new Font("SansSerif", Font.PLAIN, 24));
        newChatButton.setFocusPainted(false);
        newChatButton.setBorderPainted(false);
        newChatButton.setContentAreaFilled(false);

        JPanel newChatPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        newChatPanel.add(newChatButton);

        topBar.add(newChatPanel, BorderLayout.EAST);


        JLabel recentChatsTitle = new JLabel("Recent Chats");
        recentChatsTitle.setFont(new Font("Oxygen", Font.BOLD, 20));


        recentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        recentPanel.add(recentChatsTitle);

        recentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        recentPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, recentPanel.getPreferredSize().height));


        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setBorder(BorderFactory.createDashedBorder(Color.BLACK));
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));


        recentChatsList = new JList<>(new DefaultListModel<String>());
        JScrollPane scrollPane = new JScrollPane(recentChatsList);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());


        JPanel buttonPanel = new JPanel();
        logOut = new JButton("Logout");
        changePassword = new JButton("Change Password");


        // Action Listeners for the new buttons (Do nothing for now)
        profileButton.addActionListener(e -> {
            System.out.println("Profile Button Clicked");
        });

        newChatButton.addActionListener(e -> {
            System.out.println("New Chat Button Clicked");
        });

        // Action Listeners for existing functionality
        logOut.addActionListener(this);
        changePassword.addActionListener(
                evt -> {
                    if (evt.getSource().equals(changePassword) && changePasswordController != null) {
                        final LoggedInState currentState = loggedInViewModel.getState();
                        changePasswordController.execute(
                                currentState.getUsername(),
                                currentState.getPassword()
                        );
                    }
                }
        );
        buttonPanel.add(logOut);
        buttonPanel.add(changePassword);
        buttonPanel.setVisible(false); // Hide the buttons


        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.add(topBar);
        this.add(recentPanel);
        this.add(Box.createVerticalStrut(5)); // Small gap
        this.add(separator);
        this.add(scrollPane);
        this.add(buttonPanel);
    }

    /**
     * React to a button click that results in evt (handles Logout).
     * @param evt the ActionEvent to react to
     */
    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource().equals(logOut) && logoutController != null) {
            logoutController.execute();
        }
        System.out.println("Click " + evt.getActionCommand());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            final LoggedInState state = (LoggedInState) evt.getNewValue();
            usernameLabel.setText(state.getUsername());
        }
    }

    public String getViewName() {
        return viewName;
    }

    public void setChangePasswordController(ChangePasswordController changePasswordController) {
        this.changePasswordController = changePasswordController;
    }

    public void setLogoutController(LogoutController logoutController) {
        this.logoutController = logoutController;
    }
}