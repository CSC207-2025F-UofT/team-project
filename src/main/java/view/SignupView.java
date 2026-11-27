package view;

import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupState;
import interface_adapter.signup.SignupViewModel;
import interface_adapter.signup.SignupViewModelListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.util.Map;

/**
 * The Sign Up screen. Shows a username box, a password box, a password confirm box, a Sign Up button and a
 * "Log in" link on top of the UNO background.
 *
 * <p>Keeps the SignupState up to date as the user types, calls the SignupController when
 * the user clicks, and redraws itself when the SignupViewModel changes.</p>
 */
public class SignupView extends BackgroundPanel implements SignupViewModelListener {
    private static final int FORM_WIDTH = 380;   // width of the boxes and the button
    private static final Color LINK_ORANGE = new Color(232, 150, 30);

    private final JTextField usernameInputField = new JTextField(15);
    private final JPasswordField passwordInputField = new JPasswordField(15);
    private final JPasswordField passwordconfirmInputField = new JPasswordField(15);
    private final JLabel errorLabel = new JLabel(" ");
    private final RoundedButton signUpButton = new RoundedButton("Sign Up", FORM_WIDTH, 60);
    private final JLabel loginLink = new JLabel("Login");

    private final SignupViewModel signupViewModel;
    private SignupController signupController;

    /**
     * Builds the sign up screen and starts listening to its view model.
     *
     * @param signupViewModel the view model this screen displays
     */
    public SignupView(SignupViewModel signupViewModel) {
        this.signupViewModel = signupViewModel;
        this.signupViewModel.addListener(this);

        setPreferredSize(new Dimension(1080, 610));
        setLayout(new BorderLayout());

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Create an Account");
        title.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 36));
        title.setForeground(new Color(255, 255, 255, 215));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        RoundedInputField usernameBox = new RoundedInputField(
                usernameInputField, "Username", RoundedInputField.IconType.USER);
        RoundedInputField passwordBox = new RoundedInputField(
                passwordInputField, "Password", RoundedInputField.IconType.LOCK);
        RoundedInputField passwordconfirmBox = new RoundedInputField(
                passwordconfirmInputField, "Confirm Password", RoundedInputField.IconType.LOCK);
        usernameBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordconfirmBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        signUpButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        form.add(title);
        form.add(Box.createVerticalStrut(40));  // gap under the title
        form.add(usernameBox);
        form.add(Box.createVerticalStrut(24));  // gap between the boxes
        form.add(passwordBox);
        form.add(Box.createVerticalStrut(24));  // gap between the boxes
        form.add(passwordconfirmBox);
        form.add(buildErrorArea());             // 24px gap that also holds error messages
        form.add(signUpButton);
        form.add(Box.createVerticalStrut(22));
        form.add(buildLogInRow());

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

    /** "Already have an account? Login" with "Login" as an orange link. */
    private JPanel buildLogInRow() {
        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 16);

        JLabel prompt = new JLabel("Already have an account?");
        prompt.setFont(font);
        prompt.setForeground(new Color(255, 255, 255, 225));

        loginLink.setFont(font);
        loginLink.setForeground(LINK_ORANGE);
        loginLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Underline the link while the mouse is over it
        Map<TextAttribute, Object> underline = Map.of(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        Font underlined = font.deriveFont(underline);
        loginLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                signupController.switchToLoginView();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                loginLink.setFont(underlined);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginLink.setFont(font);
            }
        });

        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setOpaque(false);
        row.setAlignmentX(Component.CENTER_ALIGNMENT);
        row.add(prompt);
        row.add(Box.createHorizontalStrut(4));
        row.add(loginLink);
        return row;
    }

    /**
     * Gives this screen the controller to call when the user clicks.
     *
     * @param signupController the controller for the Sign Up use case
     */
    public void setSignupController(SignupController signupController) {
        this.signupController = signupController;
    }

    /**
     * Redraws the screen from the SignupViewModel's state.
     */
    @Override
    public void stateChanged() {
        final SignupState state = signupViewModel.getState();
        // Only replace the text if it's different, so the cursor doesn't jump while typing
        if (!usernameInputField.getText().equals(state.getUsername())) {
            usernameInputField.setText(state.getUsername());
        }
        if (!new String(passwordInputField.getPassword()).equals(state.getPassword())) {
            passwordInputField.setText(state.getPassword());
        }
        if (!new String(passwordconfirmInputField.getPassword()).equals(state.getRepeatPassword())) {
            passwordconfirmInputField.setText(state.getRepeatPassword());
        }
        // A single space keeps the label's height when there's no error
        final String error = state.getSignupError();
        errorLabel.setText(error == null || error.isEmpty() ? " " : error);
    }

    /**
     * Connects the boxes and the button: typing updates the state, and clicking Sign Up
     * (or pressing Enter in any box) runs the Sign Up use case.
     */
    private void wireInputs() {
        usernameInputField.getDocument().addDocumentListener(onAnyChange(
                () -> signupViewModel.getState().setUsername(usernameInputField.getText())));
        passwordInputField.getDocument().addDocumentListener(onAnyChange(
                () -> signupViewModel.getState().setPassword(new String(passwordInputField.getPassword()))));
        passwordconfirmInputField.getDocument().addDocumentListener(onAnyChange(
                () -> signupViewModel.getState().setRepeatPassword(
                        new String(passwordconfirmInputField.getPassword()))));

        signUpButton.addActionListener(e -> submit());
        usernameInputField.addActionListener(e -> submit());
        passwordInputField.addActionListener(e -> submit());
        passwordconfirmInputField.addActionListener(e -> submit());
    }

    /**
     * Sends what's in the boxes to the Sign Up use case.
     */
    private void submit() {
        final SignupState state = signupViewModel.getState();
        signupController.execute(state.getUsername(), state.getPassword(), state.getRepeatPassword());
    }

    /**
     * Makes a DocumentListener that runs the same code for any change to the text.
     *
     * @param update the code to run
     * @return the listener
     */
    private static DocumentListener onAnyChange(Runnable update) {
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