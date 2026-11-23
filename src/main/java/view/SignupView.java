package view;

import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupViewModel;
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

public class SignupView extends JPanel implements PropertyChangeListener {

    private final SignupViewModel signupViewModel;

    private final JTextField usernameField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JPasswordField repeatPasswordField = new JPasswordField(20);
    private final JLabel errorLabel = new JLabel("");

    private SignupController signupController;
    private ViewManagerModel viewManagerModel;

    public SignupView(SignupViewModel signupViewModel) {
        this.signupViewModel = signupViewModel;
        this.signupViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());

        JLabel title = new JLabel("UofTears - Sign Up", JLabel.CENTER);
        add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));

        formPanel.add(new JLabel("Username:"));
        formPanel.add(usernameField);

        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);

        formPanel.add(new JLabel("Repeat Password:"));
        formPanel.add(repeatPasswordField);

        JButton signupButton = new JButton("Sign Up");
        formPanel.add(signupButton);

        JButton backButton = new JButton("Back to Login");
        formPanel.add(backButton);

        formPanel.add(errorLabel);
        formPanel.add(new JLabel(""));
        add(formPanel, BorderLayout.CENTER);

        signupButton.addActionListener(event -> {
            if (signupController != null) {
                signupController.execute(
                        usernameField.getText(),
                        new String(passwordField.getPassword()),
                        new String(repeatPasswordField.getPassword())
                );
            }
        });

        backButton.addActionListener(event -> {
            if (viewManagerModel != null) {
                viewManagerModel.setState("login");
                viewManagerModel.firePropertyChanged();
            }
        });
    }

    public String getViewName() {
        return signupViewModel.getViewName();
    }

    public void setSignupController(SignupController signupController) {
        this.signupController = signupController;
    }

    public void setViewManagerModel(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        SignupViewModel.SignupState state = signupViewModel.getState();
        errorLabel.setText(state.getErrorMessage());
    }
}
