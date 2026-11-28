package frameworks_and_drivers;

import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;
import interface_adapter.login.LoginViewModelListener;
import interface_adapter.register.RegisterViewModel;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JPanel implements LoginViewModelListener {
    private final LoginViewModel loginViewModel;
    private LoginController loginController = null;

    private final JLabel error = new JLabel("");
    private final JTextField usernameField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);

    private final JButton loginButton = new JButton("Login");
    private final JButton registerButton = new JButton("Register");
    private final JButton forgotPasswordButton = new JButton("Forgot Password");


    public LoginView(LoginViewModel loginViewModel) {
        this.loginViewModel = loginViewModel;
        this.loginViewModel.addListener(this);

        final JLabel title = new JLabel("Login Screen");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(100, 0, 200, 0));
        this.add(title);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(new LabelTextPanel(new JLabel("Username: "), this.usernameField));
        this.add(new LabelTextPanel(new JLabel("Password: "), this.passwordField));

        JPanel buttons = new JPanel();
        buttons.add(loginButton);
        buttons.add(forgotPasswordButton);
        this.add(buttons);

        JPanel register = new JPanel();
        register.add(new JLabel("New User? "));
        register.add(this.registerButton);
        this.add(register);

        Component[] components = this.getComponents();

        for (int i = 1; i < components.length; i++) {
            components[i].setMaximumSize(components[i].getPreferredSize());
            ((JComponent) components[i]).setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        initializeListeners();
    }

    private void initializeListeners(){
        registerButton.addActionListener(e -> {
            loginController.switchToRegisterView();
        });
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    @Override
    public void stateChanged(){
        LoginState loginState = this.loginViewModel.getState();
        error.setText(loginState.getErrorMessage());
        error.setVisible(!loginState.getErrorMessage().isEmpty());
    }
}