package view;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import interface_adapter.adopter_login.AdopterLoginController;
import interface_adapter.adopter_login.AdopterLoginState;
import interface_adapter.adopter_login.AdopterLoginViewModel;
import interface_adapter.adopter_signup.AdopterSignupViewModel;
import interface_adapter.navigation.NavigationController;
import interface_adapter.role_select.RoleSelectViewModel;

/**
 * Adopter login screen: username + password, with a link to sign up.
 */
public class AdopterLoginView extends JPanel implements PropertyChangeListener {
    private final JTextField usernameField = new JTextField(15);
    private final JPasswordField passwordField = new JPasswordField(15);
    private final JLabel messageLabel = FormUtils.messageLabel();

    public AdopterLoginView(AdopterLoginViewModel viewModel,
                            AdopterLoginController controller,
                            NavigationController navigationController) {
        setLayout(new BorderLayout());
        setName(AdopterLoginViewModel.VIEW_NAME);
        viewModel.addPropertyChangeListener(this);

        final JButton loginButton = new JButton("Login");
        final JButton signupLink = new JButton("Need an account? Sign up");
        final JButton backButton = new JButton("Back");
        signupLink.setBorderPainted(false);
        signupLink.setContentAreaFilled(false);

        loginButton.addActionListener(e ->
                controller.execute(usernameField.getText(), new String(passwordField.getPassword())));
        // Pressing Enter in the password box also logs in.
        passwordField.addActionListener(e -> loginButton.doClick());
        signupLink.addActionListener(e -> {
            clearForm();
            navigationController.goTo(AdopterSignupViewModel.VIEW_NAME);
        });
        backButton.addActionListener(e -> {
            clearForm();
            navigationController.goTo(RoleSelectViewModel.VIEW_NAME);
        });

        final JPanel content = FormUtils.verticalPanel();
        content.add(FormUtils.title("Adopter Login"));
        content.add(FormUtils.row("Username:", usernameField));
        content.add(FormUtils.row("Password:", passwordField));
        content.add(messageLabel);
        content.add(FormUtils.centered(loginButton));
        content.add(FormUtils.centered(signupLink));
        content.add(FormUtils.centered(backButton));
        add(content, BorderLayout.CENTER);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final AdopterLoginState state = (AdopterLoginState) evt.getNewValue();
        if (state.getError() == null) {
            // After signup (username pre-filled) or after a successful login (cleared).
            usernameField.setText(state.getUsername());
        }
        // On an error, keep what the user typed in the username box.
        passwordField.setText("");
        FormUtils.showMessage(messageLabel, state.getError(), state.getNotice());
    }

    private void clearForm() {
        usernameField.setText("");
        passwordField.setText("");
        messageLabel.setText(" ");
    }
}
