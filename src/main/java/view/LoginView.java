
package view;

import interface_adapter.Login.*;
import interface_adapter.Signup.SignupViewModel;
import interface_adapter.ViewManagerModel;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JPanel
        implements LoginViewModelListener {

    private final LoginViewModel loginViewModel;
    private LoginController loginController;

    private final JTextField usernameField = new JTextField(15);
    private final JPasswordField passwordField = new JPasswordField(15);
    private final JLabel errorLabel = new JLabel(" ");

    public LoginView(LoginViewModel loginViewModel,
                     ViewManagerModel viewManagerModel) {

        this.loginViewModel = loginViewModel;
        loginViewModel.addListener(this);

        JLabel title = new JLabel("Login to Flicksy");

        JButton loginButton = new JButton("Login");
        JButton signupButton = new JButton("Sign Up");

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));

        form.add(new JLabel("Username:"));
        form.add(usernameField);

        form.add(new JLabel("Password:"));
        form.add(passwordField);

        errorLabel.setForeground(Color.RED);

        this.setLayout(new FlowLayout());

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        content.add(title);
        content.add(form);
        content.add(errorLabel);
        content.add(loginButton);
        content.add(signupButton);

        this.add(content);

        // Login
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            loginController.execute(username, password);
        });

        // Go to Signup
        signupButton.addActionListener(e -> {
            viewManagerModel.setState(SignupViewModel.VIEW_NAME);
            viewManagerModel.notifyViewChanged();
        });
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    @Override
    public void stateChanged() {
        LoginState state = loginViewModel.getState();

        usernameField.setText(state.getUsername());

        String error = state.getLoginError();
        errorLabel.setText(error == null ? " " : error);
    }
}
