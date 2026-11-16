package org.example.cocktailcraft.view;

import org.example.cocktailcraft.interface_adapter.NavigationController;
import org.example.cocktailcraft.interface_adapter.signup.SignupController;
import org.example.cocktailcraft.interface_adapter.signup.SignupViewModel;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class SignupView extends JPanel {
    public static final String VIEW_NAME = "signup";

    public SignupView(SignupController controller, SignupViewModel model,
                     NavigationController navigation) {
        super(new BorderLayout());
        JPanel content = ViewSupport.column();
        content.add(new JLabel("Signup - scaffold only; use dummy credentials"));
        JTextField username = new JTextField(20);
        JPasswordField password = new JPasswordField(20);
        content.add(new JLabel("Username:"));
        content.add(username);
        content.add(new JLabel("Password:"));
        content.add(password);
        JPasswordField confirmation = new JPasswordField(20);
        content.add(new JLabel("Confirm password:"));
        content.add(confirmation);
        content.add(ViewSupport.button("Signup (scaffold)", () -> {
            controller.execute(username.getText(), new String(password.getPassword()),
                    new String(confirmation.getPassword()));
            password.setText("");
            confirmation.setText("");
        }));
        content.add(ViewSupport.status(model));
        content.add(ViewSupport.button("Back to Profile", () -> navigation.show(ProfileView.VIEW_NAME)));
        add(content, BorderLayout.NORTH);
    }
}
