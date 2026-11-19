package view;

import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupState;
import interface_adapter.signup.SignupViewModel;

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
 * The signup screen.
 */
public class SignupView extends JPanel implements PropertyChangeListener {
    private final SignupViewModel signupViewModel;
    private SignupController signupController;

    private final JTextField usernameField = new JTextField(15);
    private final JPasswordField passwordField = new JPasswordField(15);
    private final JPasswordField repeatPasswordField = new JPasswordField(15);
    private final JLabel errorLabel = new JLabel(" ");

    public SignupView(SignupViewModel signupViewModel) {
        this.signupViewModel = signupViewModel;
        signupViewModel.addPropertyChangeListener(this);

        setLayout(new GridLayout(0, 1, 5, 5));
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel title = new JLabel("Create an account", JLabel.CENTER);
        errorLabel.setForeground(Color.RED);

        JButton signupButton = new JButton("Sign up");
        JButton toLoginButton = new JButton("Already have an account? Log in");

        signupButton.addActionListener(e -> signupController.execute(
                usernameField.getText(),
                new String(passwordField.getPassword()),
                new String(repeatPasswordField.getPassword())));
        toLoginButton.addActionListener(e -> signupController.switchToLoginView());

        add(title);
        add(new JLabel("Username"));
        add(usernameField);
        add(new JLabel("Password"));
        add(passwordField);
        add(new JLabel("Repeat password"));
        add(repeatPasswordField);
        add(errorLabel);
        add(signupButton);
        add(toLoginButton);
    }

    public String getViewName() {
        return signupViewModel.getViewName();
    }

    public void setSignupController(SignupController signupController) {
        this.signupController = signupController;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        SignupState state = (SignupState) evt.getNewValue();
        errorLabel.setText(state.getError() == null ? " " : state.getError());
        if (state.getError() == null) {
            passwordField.setText("");
            repeatPasswordField.setText("");
        }
    }
}
