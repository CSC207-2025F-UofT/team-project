package view;

import data_access.FileUserDataAccessObjectWithLocations;
import interface_adapter.loggedInSearchPage.LoggedInSearchPageController;
import interface_adapter.settings.SettingsController;
import interface_adapter.settings.SettingsViewModel;
import interface_adapter.settings.SettingsState;

import interface_adapter.settings.change_password.SettingsChangePasswordController;
import interface_adapter.settings.logout.SettingsLogoutController;
import interface_adapter.settings.delete_account.SettingsDeleteAccountController;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Settings view: Change password + Logout + Delete account.
 */
public class SettingsView extends JPanel implements PropertyChangeListener {

    private final String viewName = "settings";

    private final SettingsViewModel settingsViewModel;

    private SettingsChangePasswordController changePasswordController;
    private SettingsLogoutController logoutController;
    private SettingsDeleteAccountController deleteAccountController;
    private SettingsController settingsController = null;

    // UI

    private final JPasswordField newPasswordField = new JPasswordField(18);
    private final JPasswordField repeatPasswordField = new JPasswordField(18);

    private JLabel title;
    private JLabel user;

    // inline error
    private final JLabel passwordErrorLabel = new JLabel(" ");
    private final JLabel repeatErrorLabel = new JLabel(" ");
    private final JLabel usernameErrorLabel = new JLabel(" ");

    private final JButton changePasswordButton;
    private final JButton logoutButton;
    private final JButton deleteAccountButton;
    private final JButton goBack;


    public SettingsView(SettingsViewModel settingsViewModel,
                        FileUserDataAccessObjectWithLocations fileUserDataAccessObjectWithLocations) {
        this.settingsViewModel = settingsViewModel;
        this.settingsViewModel.addPropertyChangeListener(this);


        // Title
        title = new JLabel("Settings");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));

        // User
        user = new JLabel("Logged in as:" );

        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // <-- Center it horizontally
        headerPanel.add(title);
        headerPanel.add(user);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        user.setHorizontalAlignment(SwingConstants.CENTER);


        // Change password panel
        JPanel changePanel = new JPanel();

        JPanel pwRow1 = new JPanel(new BorderLayout(8, 0));
        pwRow1.add(new JLabel("New Password:"), BorderLayout.WEST);
        pwRow1.add(newPasswordField, BorderLayout.CENTER);

        JPanel pwRow2 = new JPanel(new BorderLayout(8, 0));
        pwRow2.add(new JLabel("Repeat Password:"), BorderLayout.WEST);
        pwRow2.add(repeatPasswordField, BorderLayout.CENTER);

        // make fields not tiny under BoxLayout
        newPasswordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, newPasswordField.getPreferredSize().height));
        repeatPasswordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, repeatPasswordField.getPreferredSize().height));

        passwordErrorLabel.setForeground(Color.RED);
        repeatErrorLabel.setForeground(Color.RED);

        newPasswordField.getDocument().addDocumentListener(syncListener);
        repeatPasswordField.getDocument().addDocumentListener(syncListener);

        changePasswordButton = new JButton("Change Password");
        changePasswordButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        changePanel.setLayout(new BoxLayout(changePanel, BoxLayout.Y_AXIS));
        changePanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder("Change Password"),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                )
        );
        changePanel.add(pwRow1);
        changePanel.add(Box.createVerticalStrut(8));
        changePanel.add(passwordErrorLabel);
        changePanel.add(Box.createVerticalStrut(10));
        changePanel.add(pwRow2);
        changePanel.add(Box.createVerticalStrut(8));
        changePanel.add(repeatErrorLabel);
        changePanel.add(Box.createVerticalStrut(12));
        changePanel.add(changePasswordButton);

        // Account actions panel
        JPanel accountPanel = new JPanel();

        logoutButton = new JButton("Log Out");
        logoutButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        deleteAccountButton = new JButton("Delete Account");
        deleteAccountButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        // styles
        JPanel logoutWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoutWrapper.add(logoutButton);

        JPanel deleteWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        deleteWrapper.add(deleteAccountButton);

        accountPanel.setLayout(new BoxLayout(accountPanel, BoxLayout.Y_AXIS));
        accountPanel.setBorder(BorderFactory.createTitledBorder("Account Actions"));
        accountPanel.add(logoutWrapper);
        accountPanel.add(Box.createVerticalStrut(10)); // space between buttons
        accountPanel.add(deleteWrapper);


        final JPanel buttons = new JPanel();
        goBack = new JButton("Go back");
        buttons.add(goBack);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        this.add(headerPanel);
        this.add(Box.createVerticalStrut(16));
        this.add(changePanel);
        this.add(Box.createVerticalStrut(16));
        this.add(accountPanel);
        this.add(Box.createVerticalStrut(16));
        this.add(buttons);

        goBack.addActionListener(
                e -> settingsController.goBack()
        );

        deleteAccountButton.addActionListener(e -> {
            if (deleteAccountController == null) {
                JOptionPane.showMessageDialog(this, "SettingsDeleteAccountController not set.");
                return;
            }

            Object[] options = {"Yes", "No"};
            int result = JOptionPane.showOptionDialog(
                    this,
                    "Are you sure you want to delete this account? This cannot be undone.",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[1]
            );

            if (result == JOptionPane.YES_OPTION) {
                String currentUserName = fileUserDataAccessObjectWithLocations.getCurrentUsername();
                deleteAccountController.deleteAccount(currentUserName);
                logoutController.execute();
            }
        });

        logoutButton.addActionListener(e -> {
            if (logoutController == null) {
                JOptionPane.showMessageDialog(this, "SettingsLogoutController not set.");
                return;
            }
            Object[] options = {"Yes", "No"};
            int result = JOptionPane.showOptionDialog(
                    this,
                    "Are you sure you want to logout?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[1]
            );

            if (result == JOptionPane.YES_OPTION) {
                logoutController.execute();
            }
        });

        changePasswordButton.addActionListener(e -> {
            if (changePasswordController == null) {
                JOptionPane.showMessageDialog(this, "SettingsChangePasswordController not set.");
                return;
            }

            Object[] options = {"Yes", "No"};
            int result = JOptionPane.showOptionDialog(
                    this,
                    "Are you sure you want to change the password? This cannot be undone.",
                    "Confirm Change",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[1]
            );

            if (result != JOptionPane.YES_OPTION) {
                return;
            }
            SettingsState state = settingsViewModel.getState();
            changePasswordController.changePassword(
                    state.getNewPassword(),
                    state.getRepeatPassword()
            );
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("passwordError".equals(evt.getPropertyName())) {
            SettingsState state = (SettingsState) evt.getNewValue();

            // clear old errors
            passwordErrorLabel.setText(" ");
            repeatErrorLabel.setText(" ");

            if (state.getPasswordError() == null) {
                JOptionPane.showMessageDialog(this, "Password updated for " + state.getUsername());
                newPasswordField.setText("");
                repeatPasswordField.setText("");
            } else {
                passwordErrorLabel.setText(state.getPasswordError());
            }
        } else {
            SettingsState state = settingsViewModel.getState();
            user.setText("Logged in as:" + " " + state.getUsername());
        }
        if ("clear".equals(evt.getPropertyName())) {
            newPasswordField.setText("");
            repeatPasswordField.setText("");
            passwordErrorLabel.setText("");
        }

    }

    // Sync state while typing
    DocumentListener syncListener = new DocumentListener() {
        private void sync() {
            SettingsState state = settingsViewModel.getState();
            state.setNewPassword(new String(newPasswordField.getPassword()));
            state.setRepeatPassword(new String(repeatPasswordField.getPassword()));
            settingsViewModel.setState(state);
        }
        @Override public void insertUpdate(DocumentEvent e) { sync(); }
        @Override public void removeUpdate(DocumentEvent e) { sync(); }
        @Override public void changedUpdate(DocumentEvent e) { sync(); }
    };

    public String getViewName() {
        return viewName;
    }

    public void setChangePasswordController(SettingsChangePasswordController controller) {
        this.changePasswordController = controller;
    }

    public void setLogoutController(SettingsLogoutController controller) {
        this.logoutController = controller;
    }

    public void setDeleteAccountController(SettingsDeleteAccountController controller) {
        this.deleteAccountController = controller;
    }

    public void setSettingsController(SettingsController controller) {
        this.settingsController = controller;
    }
}
