
package view;

import interface_adapter.Signup.*;

import javax.swing.*;
import java.awt.*;

public class SignupView extends JPanel
        implements SignUpViewModelListener {

    private final SignupViewModel signupViewModel;
    private SignupController signupController;

    private final JTextField usernameField = new JTextField(15);
    private final JPasswordField passwordField = new JPasswordField(15);
    private final JPasswordField repeatPasswordField = new JPasswordField(15);

    private final JLabel errorLabel = new JLabel(" ");

    public SignupView(SignupViewModel signupViewModel) {

        this.signupViewModel = signupViewModel;
        signupViewModel.addListener(this);

        JLabel title = new JLabel("Create Flicksy Account");

        JButton signupButton = new JButton("Sign Up");
        JButton loginButton = new JButton("Back to Login");

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));

        form.add(new JLabel("Username:"));
        form.add(usernameField);

        form.add(new JLabel("Password:"));
        form.add(passwordField);

        form.add(new JLabel("Confirm Password:"));
        form.add(repeatPasswordField);

        errorLabel.setForeground(Color.RED);

        this.setLayout(new FlowLayout());

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        content.add(title);
        content.add(form);
        content.add(errorLabel);
        content.add(signupButton);
        content.add(loginButton);

        this.add(content);

        // Create account
        signupButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String repeatPassword =
                    new String(repeatPasswordField.getPassword());

            signupController.execute(
                    username, password, repeatPassword);
        });

        // Back to Login
        loginButton.addActionListener(e ->
                signupController.switchToLoginView());
    }

    public void setSignupController(SignupController signupController) {
        this.signupController = signupController;
    }

    @Override
    public void stateChanged() {
        SignupState state = signupViewModel.getState();

        String error = state.getUsernameError();
        errorLabel.setText(error == null ? " " : error);
    }
}
