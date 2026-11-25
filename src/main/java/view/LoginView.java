package view;

import interface_adapter.login.LoginController;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;

import interface_adapter.login.LoginViewModelListener;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The View for when the user is logging into the program.
 */
public class LoginView extends JPanel implements ActionListener, LoginViewModelListener {

    private final LoginViewModel loginViewModel;

    private final JTextField usernameInputField = new JTextField(15);
    private final JLabel loginErrorField = new JLabel();

    private final JPasswordField passwordInputField = new JPasswordField(15);

    private final JButton logIn;
    private final JButton signUp;
    private final JButton googleLogin = new JButton("Continue with Google");
    private final JButton cancelGoogleLogin = new JButton("Cancel Google Sign-In");
    private LoginController loginController = null;
    private Runnable showSignup;

    public LoginView(LoginViewModel loginViewModel) {

        this.loginViewModel = loginViewModel;
        this.loginViewModel.addListener(this);

        final JLabel title = new JLabel("Login Screen");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final LabelTextPanel usernameInfo = new LabelTextPanel(
                new JLabel("Username"), usernameInputField);
        final LabelTextPanel passwordInfo = new LabelTextPanel(
                new JLabel("Password"), passwordInputField);

        final JPanel buttons = new JPanel();
        logIn = new JButton("Log In");
        buttons.add(logIn);
        signUp = new JButton("Sign Up");
        buttons.add(signUp);
        googleLogin.setVisible(false);
        buttons.add(googleLogin);
        cancelGoogleLogin.setVisible(false);
        buttons.add(cancelGoogleLogin);

        logIn.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(logIn)) {
                            final LoginState currentState = loginViewModel.getState();

                            loginController.execute(
                                    currentState.getUsername(),
                                    currentState.getPassword()
                            );
                        }
                    }
                }
        );

        signUp.addActionListener(this);

        usernameInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final LoginState currentState = loginViewModel.getState();
                currentState.setUsername(usernameInputField.getText());
                loginViewModel.setState(currentState);
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

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        passwordInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final LoginState currentState = loginViewModel.getState();
                currentState.setPassword(new String(passwordInputField.getPassword()));
                loginViewModel.setState(currentState);
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

        this.add(title);
        this.add(usernameInfo);
        this.add(loginErrorField);
        this.add(passwordInfo);
        this.add(buttons);
    }

    /**
     * React to a button click that results in evt.
     * @param evt the ActionEvent to react to
     */
    public void actionPerformed(ActionEvent evt) {
        showSignup.run();
    }

    @Override
    public void stateChanged() {
        setFields(loginViewModel.getState());
        loginErrorField.setText(loginViewModel.getState().getLoginError());
    }

    private void setFields(LoginState state) {
        if (!usernameInputField.getText().equals(state.getUsername())) {
            usernameInputField.setText(state.getUsername());
        }
        passwordInputField.setText("");
    }

    public void setShowSignup(Runnable showSignup) {
        this.showSignup = showSignup;
    }

    public void setGoogleLoginAction(Runnable googleLoginAction) {
        googleLogin.setVisible(true);
        googleLogin.addActionListener(event -> googleLoginAction.run());
    }

    public void setGoogleLoginCancelAction(Runnable action) {
        cancelGoogleLogin.addActionListener(event -> action.run());
    }

    public void setGoogleLoginPending(boolean pending) {
        cancelGoogleLogin.setVisible(pending);
        googleLogin.setEnabled(!pending);
        logIn.setEnabled(!pending);
        signUp.setEnabled(!pending);
        if (pending) {
            loginErrorField.setText("Finish signing in in your browser, or cancel here.");
        } else {
            loginErrorField.setText("");
        }
        revalidate();
        repaint();
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }
}
