package view;

import interface_adapter.login.LoginController;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;
import interface_adapter.login.LoginViewModelListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.util.Map;

/**
 * The login screen. Shows a username box, a password box, a Login button and a
 * "Sign Up" link on top of the UNO background.
 *
 * <p>Keeps the LoginState up to date as the user types, calls the LoginController when
 * the user clicks, and redraws itself when the LoginViewModel changes.</p>
 */
public class LoginView extends BackgroundPanel implements LoginViewModelListener {

    private static final int FORM_WIDTH = 380;   // width of the boxes and the button
    private static final Color LINK_ORANGE = new Color(232, 150, 30);

    private final JTextField usernameInputField = new JTextField(15);
    private final JPasswordField passwordInputField = new JPasswordField(15);
    private final JLabel errorLabel = new JLabel(" ");
    private final RoundedButton loginButton = new RoundedButton("Login", FORM_WIDTH, 60);
    private final JLabel signUpLink = new JLabel("Sign Up");

    private final LoginViewModel loginViewModel;
    private LoginController loginController;

    /**
     * Builds the login screen and starts listening to its view model.
     *
     * @param loginViewModel the view model this screen displays
     */
    public LoginView(LoginViewModel loginViewModel) {
        this.loginViewModel = loginViewModel;
        this.loginViewModel.addListener(this);

        setPreferredSize(new Dimension(1080, 610));
        setLayout(new BorderLayout());

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Welcome Back");
        title.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 36));
        title.setForeground(new Color(255, 255, 255, 215));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        RoundedInputField usernameBox = new RoundedInputField(
                usernameInputField, "Username", RoundedInputField.IconType.USER);
        RoundedInputField passwordBox = new RoundedInputField(
                passwordInputField, "Password", RoundedInputField.IconType.LOCK);
        usernameBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        form.add(title);
        form.add(Box.createVerticalStrut(40));  // gap under the title
        form.add(usernameBox);
        form.add(Box.createVerticalStrut(24));  // gap between the boxes
        form.add(passwordBox);
        form.add(buildErrorArea());             // 24px gap that also holds error messages
        form.add(loginButton);
        form.add(Box.createVerticalStrut(22));
        form.add(buildSignUpRow());

        // ---- Left column: centres the form vertically ----
        JPanel leftColumn = new JPanel(new GridBagLayout());
        leftColumn.setOpaque(false);
        leftColumn.setPreferredSize(new Dimension(460, 0));
        leftColumn.add(form);
        add(leftColumn, BorderLayout.WEST);

        wireInputs();
    }

    private JPanel buildErrorArea() {
        errorLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        errorLabel.setForeground(new Color(255, 200, 185));

        JPanel area = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 4));
        area.setOpaque(false);
        area.setAlignmentX(Component.CENTER_ALIGNMENT);
        area.add(errorLabel);

        Dimension size = new Dimension(FORM_WIDTH, 24);
        area.setPreferredSize(size);
        area.setMaximumSize(size);
        return area;
    }

    /** "Don't have an account? Sign Up" with "Sign Up" as an orange link. */
    private JPanel buildSignUpRow() {
        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 16);

        JLabel prompt = new JLabel("Don't have an account?");
        prompt.setFont(font);
        prompt.setForeground(new Color(255, 255, 255, 225));

        signUpLink.setFont(font);
        signUpLink.setForeground(LINK_ORANGE);
        signUpLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Underline the link while the mouse is over it
        Map<TextAttribute, Object> underline = Map.of(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        Font underlined = font.deriveFont(underline);
        signUpLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                loginController.switchToSignupView();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                signUpLink.setFont(underlined);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                signUpLink.setFont(font);
            }
        });

        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setOpaque(false);
        row.setAlignmentX(Component.CENTER_ALIGNMENT);
        row.add(prompt);
        row.add(Box.createHorizontalStrut(4));
        row.add(signUpLink);
        return row;
    }

    /**
     * Gives this screen the controller to call when the user clicks.
     *
     * @param loginController the controller for the Login use case
     */
    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    /**
     * Redraws the screen from the LoginViewModel's state.
     */
    @Override
    public void stateChanged() {
        final LoginState state = loginViewModel.getState();
        // Only replace the text if it's different, so the cursor doesn't jump while typing
        if (!usernameInputField.getText().equals(state.getUsername())) {
            usernameInputField.setText(state.getUsername());
        }
        if (!new String(passwordInputField.getPassword()).equals(state.getPassword())) {
            passwordInputField.setText(state.getPassword());
        }
        // A single space keeps the label's height when there's no error
        final String error = state.getLoginError();
        errorLabel.setText(error == null || error.isEmpty() ? " " : error);
    }

    /**
     * Connects the boxes and the button: typing updates the state, and clicking Login
     * (or pressing Enter in either box) runs the Login use case.
     */
    private void wireInputs() {
        usernameInputField.getDocument().addDocumentListener(
                onAnyChange(() -> loginViewModel.getState().setUsername(usernameInputField.getText())));
        passwordInputField.getDocument().addDocumentListener(
                onAnyChange(() -> loginViewModel.getState().setPassword(
                        new String(passwordInputField.getPassword()))));

        loginButton.addActionListener(e -> submit());
        usernameInputField.addActionListener(e -> submit());
        passwordInputField.addActionListener(e -> submit());
    }

    private void submit() {
        final LoginState state = loginViewModel.getState();
        loginController.execute(state.getUsername(), state.getPassword());
    }

    /**
     * Makes a DocumentListener that runs the same code for any change to the text.
     *
     * @param update the code to run
     * @return the listener
     */
    static DocumentListener onAnyChange(Runnable update) {
        return new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                update.run();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update.run();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update.run();
            }
        };
    }
}