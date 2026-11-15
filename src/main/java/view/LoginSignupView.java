package view;

import interface_adapter.login.LoginController;
import interface_adapter.signup.SignupController;
import interface_adapter.login_signup.LoginSignupState;
import interface_adapter.login_signup.LoginSignupViewModel;
import interface_adapter.login_signup.LoginSignupViewModelListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;



/**
 * The View for the Login and Signup Use Cases.
 */
public class LoginSignupView extends JPanel implements ActionListener, LoginSignupViewModelListener {

    private final LoginSignupViewModel loginSignupViewModel;
    private final JTextField usernameInputField = new JTextField(15);
    private final JPasswordField passwordInputField = new JPasswordField(15);
    private final JButton logIn;
    private final JButton signUp;
    private SignupController signupController = null;
    private LoginController loginController = null;

    public LoginSignupView(LoginSignupViewModel loginSignupViewModel) {
        this.loginSignupViewModel = loginSignupViewModel;
        loginSignupViewModel.addListener(this);

        final JPanel buttons = new JPanel();
        logIn = new JButton(LoginSignupViewModel.LOGIN_BUTTON_LABEL);
        buttons.add(logIn);
        signUp = new JButton(LoginSignupViewModel.SIGNUP_BUTTON_LABEL);
        buttons.add(signUp);

        signUp.addActionListener(this);
        logIn.addActionListener(this);

        addDocumentListener(usernameInputField, () ->
                loginSignupViewModel.getState().setUsername(usernameInputField.getText()));
        addDocumentListener(passwordInputField, () ->
                loginSignupViewModel.getState().setPassword(new String(passwordInputField.getPassword())));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(labelledRow(LoginSignupViewModel.USERNAME_LABEL, usernameInputField));
        add(labelledRow(LoginSignupViewModel.PASSWORD_LABEL, passwordInputField));
        add(buttons);
    }

    private JPanel labelledRow(String label, JComponent field){
        final JPanel row = new JPanel();
        row.add(new JLabel(label));
        row.add(field);
        return row;
    }

    private void addDocumentListener(JTextComponent field, Runnable onChange) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                onChange.run();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                onChange.run();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                onChange.run();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        final LoginSignupState state = loginSignupViewModel.getState();
        if (evt.getSource().equals(signUp)) {
            signupController.execute(state.getUsername(), state.getPassword());
        }
        else if (evt.getSource().equals(logIn)) {
            loginController.execute(state.getUsername(), state.getPassword());
        }
    }

    @Override
    public void stateChanged() {
        final LoginSignupState state = loginSignupViewModel.getState();
        setFields(state);
        if (!state.getSignupError().isEmpty()) {
            final String error = state.getSignupError();
            state.setSignupError("");
            JOptionPane.showMessageDialog(this, error);
        }
        if (!state.getLoginError().isEmpty()){
            final String error = state.getLoginError();
            state.setLoginError("");
            JOptionPane.showMessageDialog(this, error);
        }
    }

    private void setFields(LoginSignupState state) {
        usernameInputField.setText(state.getUsername());
        passwordInputField.setText(state.getPassword());
    }

    public void setSignupController(SignupController signupController) {
        this.signupController = signupController;
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }
}
