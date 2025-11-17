package app;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * A complete standalone login/logout manager with:
 * - in-memory user authentication
 * - simple user progress storage
 * - Swing login dialog
 * - Swing logout confirmation dialog
 *
 * This file contains NO TODOs.
 * It runs fully as-is.
 */
public final class AuthManager {

    // ---- Authentication State ----

    private static String currentUser = null; // null = not logged in

    // Simple in-memory progress store: each user gets its own map
    private static final Map<String, Map<String, String>> userProgress = new HashMap<>();

    private AuthManager() {}

    // ---- Public APIs ----

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static String getCurrentUser() {
        return currentUser;
    }

    /**
     * Show the login dialog (modal). Calls onLoginChange.run() on successful login.
     */
    public static void showLoginDialog(Window parent, Runnable onLoginChange) {
        LoginDialog dialog = new LoginDialog(parent, onLoginChange);
        dialog.setVisible(true);
    }

    /**
     * Show logout confirmation dialog (modal). Calls onLoginChange.run() after logout.
     */
    public static void showLogoutDialog(Window parent, Runnable onLoginChange) {
        LogoutDialog dialog = new LogoutDialog(parent, onLoginChange);
        dialog.setVisible(true);
    }


    // ---- Internal Authentication Logic ----

    private static boolean validateCredentials(String username, String password) {
        // Hard-coded user database for now
        return (username.equals("alice") && password.equals("password123"))
                || (username.equals("bob") && password.equals("hunter2"));
    }

    private static void login(String username) {
        currentUser = username;

        // Create progress map for the user if not present
        userProgress.putIfAbsent(username, new HashMap<>());
    }

    private static void logout() {
        currentUser = null;
    }

    // ---- Public Progress API ----

    public static Map<String, String> getProgressForCurrentUser() {
        if (currentUser == null) return new HashMap<>();
        return userProgress.getOrDefault(currentUser, new HashMap<>());
    }

    public static void saveProgress(String key, String value) {
        if (currentUser == null) return;
        userProgress.get(currentUser).put(key, value);
    }

    public static void clearProgressForCurrentUser() {
        if (currentUser == null) return;
        userProgress.get(currentUser).clear();
    }


    // ---- Inner Dialog Classes ----

    /**
     * Login dialog with username + password.
     */
    private static class LoginDialog extends JDialog {

        public LoginDialog(Window parent, Runnable onLoginChange) {
            super(parent, "Login", ModalityType.APPLICATION_MODAL);

            JTextField usernameField = new JTextField(15);
            JPasswordField passwordField = new JPasswordField(15);

            JLabel errorLabel = new JLabel(" ");
            errorLabel.setForeground(Color.RED);

            JButton loginButton = new JButton("Login");

            loginButton.addActionListener(e -> {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword());

                if (validateCredentials(username, password)) {
                    login(username);
                    onLoginChange.run();
                    dispose();
                } else {
                    errorLabel.setText("Invalid username or password.");
                }
            });

            setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(6, 6, 6, 6);

            c.gridx = 0; c.gridy = 0; c.anchor = GridBagConstraints.LINE_END;
            add(new JLabel("Username:"), c);

            c.gridx = 1; c.gridy = 0; c.anchor = GridBagConstraints.LINE_START;
            add(usernameField, c);

            c.gridx = 0; c.gridy = 1; c.anchor = GridBagConstraints.LINE_END;
            add(new JLabel("Password:"), c);

            c.gridx = 1; c.gridy = 1; c.anchor = GridBagConstraints.LINE_START;
            add(passwordField, c);

            c.gridx = 1; c.gridy = 2; c.anchor = GridBagConstraints.CENTER;
            add(loginButton, c);

            c.gridx = 1; c.gridy = 3; c.anchor = GridBagConstraints.CENTER;
            add(errorLabel, c);

            pack();
            setLocationRelativeTo(parent);
        }
    }


    /**
     * Logout confirmation dialog.
     */
    private static class LogoutDialog extends JDialog {

        public LogoutDialog(Window parent, Runnable onLoginChange) {
            super(parent, "Confirm Logout", ModalityType.APPLICATION_MODAL);

            JLabel msg = new JLabel("You will be logged out now.");
            JButton confirmButton = new JButton("Confirm");

            confirmButton.addActionListener(e -> {
                logout();
                onLoginChange.run();
                dispose();
            });

            JPanel panel = new JPanel(new FlowLayout());
            panel.add(msg);
            panel.add(confirmButton);

            setContentPane(panel);

            pack();
            setLocationRelativeTo(parent);
        }
    }
}
