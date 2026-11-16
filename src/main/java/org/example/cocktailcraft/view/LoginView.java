package org.example.cocktailcraft.view;

import org.example.cocktailcraft.interface_adapter.NavigationController;
import org.example.cocktailcraft.interface_adapter.login.LoginController;
import org.example.cocktailcraft.interface_adapter.login.LoginViewModel;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginView extends JPanel {
    public static final String VIEW_NAME = "login";

    public LoginView(LoginController controller, LoginViewModel model,
                     NavigationController navigation) {
        super(new BorderLayout());
        JPanel content = ViewSupport.column();
        content.add(new JLabel("Login - scaffold only; use dummy credentials"));
        JTextField username = new JTextField(20);
        JPasswordField password = new JPasswordField(20);
        content.add(new JLabel("Username:"));
        content.add(username);
        content.add(new JLabel("Password:"));
        content.add(password);

        content.add(ViewSupport.button("Login (scaffold)", () -> {
            controller.execute(username.getText(), new String(password.getPassword()));
            password.setText("");
        }));
        content.add(ViewSupport.status(model));
        content.add(ViewSupport.button("Back to Profile", () -> navigation.show(ProfileView.VIEW_NAME)));
        add(content, BorderLayout.NORTH);
    }
}
