package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import interface_adapter.Login.LoginController;
import interface_adapter.Login.LoginState;
import interface_adapter.Login.LoginViewModel;
import interface_adapter.Login.LoginViewModelListener;

/**
 * The View for when the user is logging into the program.
 */
public class LoginView extends JPanel implements ActionListener, LoginViewModelListener {

    private final LoginViewModel loginViewModel;

    private final JTextField usernameInputField = new JTextField(15);
    private final JLabel welcomeField = new JLabel();
    private final JLabel loginErrorField = new JLabel();

    private final JButton logIn;
    private final JButton signUp;
    private LoginController loginController = null;

    public LoginView(LoginViewModel loginViewModel) {
        this.loginViewModel = loginViewModel;
        this.loginViewModel.addListener(this);

        final JLabel title = new JLabel("Login Screen");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final LabelTextPanel usernameInfo = new LabelTextPanel(
                new JLabel("Username"), usernameInputField);

        welcomeField.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginErrorField.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginErrorField.setForeground(Color.RED);

        final JPanel buttons = new JPanel();
        logIn = new JButton("log in");
        buttons.add(logIn);
        signUp = new JButton("sign up");
        buttons.add(signUp);

        logIn.addActionListener(this);
        signUp.addActionListener(this);

        usernameInputField.getDocument().addDocumentListener(new SimpleDocumentListener(() -> {
            final LoginState currentState = loginViewModel.getState();
            currentState.setUsername(usernameInputField.getText());
            loginViewModel.setState(currentState);
        }));

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(title);
        this.add(welcomeField);
        this.add(usernameInfo);
        this.add(loginErrorField);
        this.add(buttons);
    }

    /**
     * React to a button click that results in evt.
     * @param evt the ActionEvent to react to
     */
    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource().equals(logIn)) {
            loginController.execute(loginViewModel.getState().getUsername());
        }
        else if (evt.getSource().equals(signUp)) {
            loginController.switchToSignupView();
        }
    }

    @Override
    public void stateChanged() {
        final LoginState state = loginViewModel.getState();
        if (!usernameInputField.getText().equals(state.getUsername())) {
            usernameInputField.setText(state.getUsername());
        }
        welcomeField.setText(state.getWelcomeMessage());
        loginErrorField.setText(state.getLoginError());
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }
}
