package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupState;
import interface_adapter.signup.SignupViewModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The View for the Signup screen.
 *
 * Displays input fields for username, password, and confirm password,
 * along with a signup button. Observes the SignupViewModel and updates
 * the display when the state changes (e.g., to show error messages).
 */
public class SignupView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "sign up";
    private final SignupViewModel signupViewModel;
    private final ViewManagerModel viewManagerModel;

    private final JTextField usernameInputField = new JTextField(15);
    private final JPasswordField passwordInputField = new JPasswordField(15);
    private final JPasswordField confirmPasswordInputField = new JPasswordField(15);

    private final JLabel usernameErrorField = new JLabel();
    private final JLabel passwordErrorField = new JLabel();
    private final JLabel successField = new JLabel();

    private final JButton signUpButton;
    private final JButton loginButton;

    private SignupController signupController;

    /**
     * Constructs the Signup View.
     *
     * Sets up all UI components and registers as an observer of the
     * SignupViewModel to receive state change notifications.
     *
     * @param signupViewModel the view model managing this view's state
     */
    public SignupView(SignupViewModel signupViewModel, ViewManagerModel viewManagerModel) {
        this.signupViewModel = signupViewModel;
        this.signupViewModel.addPropertyChangeListener(this);
        this.viewManagerModel = viewManagerModel;

        // Title
        final JLabel title = new JLabel("Sign Up Screen");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Input fields
        final LabelTextPanel usernameInfo = new LabelTextPanel(
                new JLabel("Username"), usernameInputField);
        final LabelTextPanel passwordInfo = new LabelTextPanel(
                new JLabel("Password"), passwordInputField);
        final LabelTextPanel confirmPasswordInfo = new LabelTextPanel(
                new JLabel("Confirm Password"), confirmPasswordInputField);

        // Error and success labels with fixed size
        usernameErrorField.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameErrorField.setHorizontalAlignment(SwingConstants.CENTER);
        usernameErrorField.setForeground(Color.RED);
        usernameErrorField.setPreferredSize(new Dimension(300, 20));
        usernameErrorField.setMinimumSize(new Dimension(300, 20));
        usernameErrorField.setMaximumSize(new Dimension(300, 20));
        usernameErrorField.setText(" ");

        passwordErrorField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordErrorField.setHorizontalAlignment(SwingConstants.CENTER);
        passwordErrorField.setForeground(Color.RED);
        passwordErrorField.setPreferredSize(new Dimension(300, 20));
        passwordErrorField.setMinimumSize(new Dimension(300, 20));
        passwordErrorField.setMaximumSize(new Dimension(300, 20));
        passwordErrorField.setText(" ");

        successField.setAlignmentX(Component.CENTER_ALIGNMENT);
        successField.setHorizontalAlignment(SwingConstants.CENTER);
        successField.setForeground(Color.GREEN);
        successField.setPreferredSize(new Dimension(300, 20));
        successField.setMinimumSize(new Dimension(300, 20));
        successField.setMaximumSize(new Dimension(300, 20));
        successField.setText(" ");

        // Buttons
        final JPanel buttons = new JPanel();
        signUpButton = new JButton("Sign Up");
        buttons.add(signUpButton);

        loginButton = new JButton("Login");
        buttons.add(loginButton);

        // Sign Up button action
        signUpButton.addActionListener(
                evt -> {
                    if (evt.getSource().equals(signUpButton)) {
                        final SignupState currentState = signupViewModel.getState();

                        signupController.execute(
                                currentState.getUsername(),
                                currentState.getPassword(),
                                currentState.getConfirmPassword()
                        );
                    }
                }
        );

        // go back to log in
        loginButton.addActionListener(
                evt -> {
                    if (evt.getSource().equals(loginButton)) {
                        signupViewModel.setState(new SignupState());
                        signupViewModel.firePropertyChange();

                        viewManagerModel.setState("log in");
                        viewManagerModel.firePropertyChange();
                    }
                }
        );

        // Username field listener
        usernameInputField.getDocument().addDocumentListener(new DocumentListener() {
            private void documentListenerHelper() {
                final SignupState currentState = signupViewModel.getState();
                currentState.setUsername(usernameInputField.getText());
                signupViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
        });

        // Password field listener
        passwordInputField.getDocument().addDocumentListener(new DocumentListener() {
            private void documentListenerHelper() {
                final SignupState currentState = signupViewModel.getState();
                currentState.setPassword(new String(passwordInputField.getPassword()));
                signupViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
        });

        // Confirm Password field listener
        confirmPasswordInputField.getDocument().addDocumentListener(new DocumentListener() {
            private void documentListenerHelper() {
                final SignupState currentState = signupViewModel.getState();
                currentState.setConfirmPassword(new String(confirmPasswordInputField.getPassword()));
                signupViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
        });

        // Layout
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(title);
        this.add(usernameInfo);
        this.add(usernameErrorField);
        this.add(passwordInfo);
        this.add(passwordErrorField);
        this.add(confirmPasswordInfo);
        this.add(successField);
        this.add(buttons);
    }

    /**
     * React to a button click.
     * @param evt the ActionEvent to react to
     */
    @Override
    public void actionPerformed(ActionEvent evt) {
        System.out.println("Click " + evt.getActionCommand());
    }

    /**
     * Called when the SignupViewModel's state changes.
     *
     * Updates the error message display based on the new state.
     *
     * @param evt the property change event containing the new state
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final SignupState state = (SignupState) evt.getNewValue();
        setFields(state);

        usernameErrorField.setText(state.getUsernameError());
        passwordErrorField.setText(state.getPasswordError());
        successField.setText(state.getSuccessMessage());
    }

    private void setFields(SignupState state) {
        usernameInputField.setText(state.getUsername());
        passwordInputField.setText(state.getPassword());
        confirmPasswordInputField.setText(state.getConfirmPassword());
    }

    public String getViewName() {
        return viewName;
    }

    public void setSignupController(SignupController signupController) {
        this.signupController = signupController;
    }
}