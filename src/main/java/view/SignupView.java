package view;

import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupState;
import interface_adapter.signup.SignupViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class SignupView extends JPanel
        implements PropertyChangeListener {

    private final SignupController signupController;
    private final SignupViewModel signupViewModel;

    private final JTextField usernameField = new JTextField(15);
    private final JPasswordField passwordField = new JPasswordField(15);
    private final JPasswordField repeatPasswordField =
            new JPasswordField(15);

    private final JLabel errorLabel = new JLabel("");

    public SignupView(SignupController signupController,
                      SignupViewModel signupViewModel) {

        this.signupController = signupController;
        this.signupViewModel = signupViewModel;

        signupViewModel.addPropertyChangeListener(this);

        setLayout(new GridLayout(5, 2));

        add(new JLabel("Username:"));
        add(usernameField);

        add(new JLabel("Password:"));
        add(passwordField);

        add(new JLabel("Repeat Password:"));
        add(repeatPasswordField);

        JButton signupButton = new JButton("Sign Up");
        add(signupButton);

        add(errorLabel);

        signupButton.addActionListener(event -> {
            signupController.execute(
                    usernameField.getText(),
                    new String(passwordField.getPassword()),
                    new String(repeatPasswordField.getPassword())
            );
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {

        SignupState state = signupViewModel.getState();

        if (state.getSignupError() != null) {
            errorLabel.setText(state.getSignupError());
        } else {
            errorLabel.setText("Signup successful!");
        }
    }
}
