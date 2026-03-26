package view;

import interface_adapter.logout.LogoutController;
import interface_adapter.signin.SignInViewModel;
import interface_adapter.signin.SignInState;
import interface_adapter.signin.SignInController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class SignInView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "Sign in view";

    private final SignInViewModel signInViewModel;
    private SignInController signInController = null;

    // Fields
    private final JTextField usernameInputField = new JTextField(15);
    private final JLabel usernameErrorLabel = new JLabel();

    private final JPasswordField passwordInputField = new JPasswordField(15);
    private final JLabel passwordErrorLabel = new JLabel();

    // Buttons
    private final JButton signInButton;
    private final JButton backToHomeButton;

    public SignInView(SignInViewModel signInViewModel) {
        this.signInViewModel = signInViewModel;
        this.signInViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(20, 20, 20, 20));
        add(content, BorderLayout.CENTER);

        JLabel title = new JLabel("Sign In");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));

        JLabel usernameLabel = new JLabel("Username: ");
        JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameInputField);

        usernameErrorLabel.setForeground(Color.RED);
        usernameErrorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel passwordLabel = new JLabel("Password: ");
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordInputField);

        passwordErrorLabel.setForeground(Color.RED);
        passwordErrorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        signInButton = new JButton("Sign in");
        backToHomeButton = new JButton("Go back");
        signInButton.addActionListener(this);
        backToHomeButton.addActionListener(this);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonsPanel.add(signInButton);
        buttonsPanel.add(backToHomeButton);

        // IMPORTANT: keep panels centered in BoxLayout
        usernamePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        usernameErrorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        passwordErrorLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // make BoxLayout NOT stretch these vertically
        lockHeight(usernamePanel);
        lockHeight(passwordPanel);
        lockHeight(buttonsPanel);
        lockHeight(usernameErrorLabel);
        lockHeight(passwordErrorLabel);

        content.add(title);
        content.add(Box.createVerticalStrut(15));
        content.add(usernamePanel);
        content.add(usernameErrorLabel);
        content.add(Box.createVerticalStrut(10));
        content.add(passwordPanel);
        content.add(passwordErrorLabel);
        content.add(Box.createVerticalStrut(10));
        content.add(buttonsPanel);
        content.add(Box.createVerticalGlue());
    }

    private static void lockHeight(JComponent c) {
        Dimension pref = c.getPreferredSize();
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, pref.height));
    }


    public String getViewName() {
        return viewName;
    }

    public void setSignInController(SignInController signInController) {
        this.signInController = signInController;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == signInButton) {
            String username = usernameInputField.getText();
            String password = new String(passwordInputField.getPassword());

            if (signInController != null) {
                // main sign in / create-account action
                signInController.execute(username, password);
            }

        } else if (source == backToHomeButton) {
            if (signInController != null) {
                // you implement this in controller (change view to homepage)
                signInController.switchToHomePage();
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!"state".equals(evt.getPropertyName())) {
            return;
        }

        SignInState state = (SignInState) evt.getNewValue();

        String newUsername = state.getUsername();

        if (newUsername != null && !newUsername.equals(usernameInputField.getText())) {
            usernameInputField.setText(newUsername);
        }

        String newPassword = state.getPassword();
        if (newPassword != null) {
            passwordInputField.setText(newPassword);
        }

        // If your state stores the text, you can sync them here (optional):
        // usernameInputField.setText(state.getUsername());
        String signinError = state.getSigninError();
        if (!"".equals(signinError)) {
            usernameErrorLabel.setText(state.getUsernameError());
            passwordErrorLabel.setText(state.getPasswordError());
        }
    }
}
