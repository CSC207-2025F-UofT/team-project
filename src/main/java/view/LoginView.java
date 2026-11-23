package view;

import interface_adapter.login.LoginController;
import interface_adapter.login.LoginViewModel;
import interface_adapter.ViewManagerModel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginView extends JPanel implements PropertyChangeListener{

    private final LoginViewModel loginViewModel;
    private ViewManagerModel viewManagerModel;

    private final JTextField usernameField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JLabel errorLabel = new JLabel("");

    private LoginController loginController;

    public LoginView(LoginViewModel loginViewModel){
        this.loginViewModel = loginViewModel;
        this.loginViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());

        JLabel title = new JLabel("UofTears - Login", JLabel.CENTER);
        add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        formPanel.add(new JLabel("Username:"));
        formPanel.add(usernameField);

        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);

        JButton loginButton = new JButton("Login");
        formPanel.add(loginButton);
        formPanel.add(errorLabel);

        JButton signupButton = new JButton("Sign Up");
        add(signupButton, BorderLayout.SOUTH);

        signupButton.addActionListener(event -> {
            if (viewManagerModel != null) {
                viewManagerModel.setState("signup");
                viewManagerModel.firePropertyChanged();
            }
        });

        add(formPanel, BorderLayout.CENTER);

        loginButton.addActionListener(event -> {
            if (loginController != null){
                loginController.execute(usernameField.getText(), new String(passwordField.getPassword()));
            }
        });
    }

    public String getViewName(){
        return loginViewModel.getViewName();
    }

    public void setLoginController(LoginController loginController){
        this.loginController = loginController;
    }

    public void setViewManagerModel(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
    }

    public void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        errorLabel.setText("");
    }

    @Override
    public void propertyChange(PropertyChangeEvent event){
        LoginViewModel.LoginState state = loginViewModel.getState();
        errorLabel.setText(state.getErrorMessage());
    }
}
