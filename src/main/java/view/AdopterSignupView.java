package view;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import interface_adapter.adopter_login.AdopterLoginViewModel;
import interface_adapter.adopter_signup.AdopterSignupController;
import interface_adapter.adopter_signup.AdopterSignupState;
import interface_adapter.adopter_signup.AdopterSignupViewModel;
import interface_adapter.navigation.NavigationController;
import interface_adapter.role_select.RoleSelectViewModel;

/**
 * Adopter signup screen: username, password and repeat password.
 */
public class AdopterSignupView extends JPanel implements PropertyChangeListener {
    private final JTextField usernameField = new JTextField(15);
    private final JPasswordField passwordField = new JPasswordField(15);
    private final JPasswordField repeatPasswordField = new JPasswordField(15);
    private final JLabel messageLabel = FormUtils.messageLabel();

    public AdopterSignupView(AdopterSignupViewModel viewModel,
                             AdopterSignupController controller,
                             NavigationController navigationController) {
        setLayout(new BorderLayout());
        setName(AdopterSignupViewModel.VIEW_NAME);
        viewModel.addPropertyChangeListener(this);

        final JButton signupButton = new JButton("Sign up");
        final JButton loginLink = new JButton("Already have an account? Log in");
        final JButton backButton = new JButton("Back");
        loginLink.setBorderPainted(false);
        loginLink.setContentAreaFilled(false);

        signupButton.addActionListener(e -> controller.execute(
                usernameField.getText(),
                new String(passwordField.getPassword()),
                new String(repeatPasswordField.getPassword())));
        repeatPasswordField.addActionListener(e -> signupButton.doClick());
        loginLink.addActionListener(e -> {
            clearForm();
            navigationController.goTo(AdopterLoginViewModel.VIEW_NAME);
        });
        backButton.addActionListener(e -> {
            clearForm();
            navigationController.goTo(RoleSelectViewModel.VIEW_NAME);
        });

        final JPanel content = FormUtils.verticalPanel();
        content.add(FormUtils.title("Create Adopter Account"));
        content.add(FormUtils.row("Username:", usernameField));
        content.add(FormUtils.row("Password:", passwordField));
        content.add(FormUtils.row("Repeat password:", repeatPasswordField));
        content.add(messageLabel);
        content.add(FormUtils.centered(signupButton));
        content.add(FormUtils.centered(loginLink));
        content.add(FormUtils.centered(backButton));
        add(content, BorderLayout.CENTER);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final AdopterSignupState state = (AdopterSignupState) evt.getNewValue();
        if (state.getError() == null) {
            // Signup succeeded (or the form was reset): clear everything.
            clearForm();
        }
        else {
            // Keep the username so the user doesn't have to retype it; clear passwords.
            passwordField.setText("");
            repeatPasswordField.setText("");
            FormUtils.showMessage(messageLabel, state.getError(), null);
        }
    }

    private void clearForm() {
        usernameField.setText("");
        passwordField.setText("");
        repeatPasswordField.setText("");
        messageLabel.setText(" ");
    }
}
