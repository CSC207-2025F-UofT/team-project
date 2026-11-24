package view;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import interface_adapter.admin_login.AdminLoginController;
import interface_adapter.admin_login.AdminLoginState;
import interface_adapter.admin_login.AdminLoginViewModel;
import interface_adapter.navigation.NavigationController;
import interface_adapter.role_select.RoleSelectViewModel;

/**
 * Admin login screen: only a password.
 */
public class AdminLoginView extends JPanel implements PropertyChangeListener {
    private final JPasswordField passwordField = new JPasswordField(15);
    private final JLabel messageLabel = FormUtils.messageLabel();

    public AdminLoginView(AdminLoginViewModel viewModel,
                          AdminLoginController controller,
                          NavigationController navigationController) {
        setLayout(new BorderLayout());
        setName(AdminLoginViewModel.VIEW_NAME);
        viewModel.addPropertyChangeListener(this);

        final JButton loginButton = new JButton("Login");
        final JButton backButton = new JButton("Back");

        loginButton.addActionListener(e -> controller.execute(new String(passwordField.getPassword())));
        passwordField.addActionListener(e -> loginButton.doClick());
        backButton.addActionListener(e -> {
            passwordField.setText("");
            messageLabel.setText(" ");
            navigationController.goTo(RoleSelectViewModel.VIEW_NAME);
        });

        final JPanel content = FormUtils.verticalPanel();
        content.add(FormUtils.title("Admin Login"));
        content.add(FormUtils.row("Password:", passwordField));
        content.add(messageLabel);
        content.add(FormUtils.centered(loginButton));
        content.add(FormUtils.centered(backButton));
        add(content, BorderLayout.CENTER);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final AdminLoginState state = (AdminLoginState) evt.getNewValue();
        passwordField.setText("");
        FormUtils.showMessage(messageLabel, state.getError(), null);
    }
}
