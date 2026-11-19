package view;

import interface_adapter.login.LoginController;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The login screen (first screen the user sees).
 */
public class LoginView extends JPanel implements PropertyChangeListener {
    private final LoginViewModel loginViewModel;
    private LoginController loginController;

    private final JTextField usernameField = new JTextField(15);
    private final JPasswordField passwordField = new JPasswordField(15);
    private final JLabel errorLabel = new JLabel(" ");

    public LoginView(LoginViewModel loginViewModel) {
        this.loginViewModel = loginViewModel;
        loginViewModel.addPropertyChangeListener(this);

        setLayout(new GridLayout(0, 1, 5, 5));
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel title = new JLabel("Log in", JLabel.CENTER);
        errorLabel.setForeground(Color.RED);

        JButton loginButton = new JButton("Log in");
        JButton toSignupButton = new JButton("No account? Sign up");

        loginButton.addActionListener(e -> loginController.execute(
                usernameField.getText(),
                new String(passwordField.getPassword())));
        toSignupButton.addActionListener(e -> loginController.switchToSignupView());

        add(title);
        add(new JLabel("Username"));
        add(usernameField);
        add(new JLabel("Password"));
        add(passwordField);
        add(errorLabel);
        add(loginButton);
        add(toSignupButton);
    }

    public String getViewName() {
        return loginViewModel.getViewName();
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        LoginState state = (LoginState) evt.getNewValue();
        usernameField.setText(state.getUsername());
        errorLabel.setText(state.getError() == null ? " " : state.getError());
        passwordField.setText("");
    }
}
