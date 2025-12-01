package view;

import javax.swing.*;
import java.awt.*;

public class CreateUserAccountView extends JPanel {
    private final String viewName = "CreateUserAccount";

    private final JTextField usernameField = new JTextField(15);
    private final JPasswordField passwordField = new JPasswordField(15);
    private final JPasswordField confirmPasswordField = new JPasswordField(15);
    private final JLabel errorLabel = new JLabel(" ");

    private final JButton signUpButton = new JButton("Sign Up");
    private final JButton cancelButton = new JButton("Cancel");

    public CreateUserAccountView() {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel title = new JLabel("Create User Account");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);
        add(Box.createVerticalStrut(15));

        // Username row
        add(createRow("Username", usernameField));

        // Password row
        add(createRow("Build password", passwordField));

        // Confirm Password row
        add(createRow("Confirm password", confirmPasswordField));

        // Error text
        errorLabel.setForeground(Color.RED);
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(errorLabel);
        add(Box.createVerticalStrut(15));

        // Button row
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        btnPanel.add(signUpButton);
        btnPanel.add(cancelButton);

        add(btnPanel);

        // Events
        signUpButton.addActionListener(e -> checkPasswords());
        cancelButton.addActionListener(e -> clearFields());
    }

    /** Creates one row (label + textfield) */
    private JPanel createRow(String labelText, JComponent field) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(140, 25)); // align nicely

        panel.add(label);
        panel.add(field);

        return panel;
    }

    /** Validate password match */
    private void checkPasswords() {
        String p1 = new String(passwordField.getPassword());
        String p2 = new String(confirmPasswordField.getPassword());

        if (!p1.equals(p2)) {
            errorLabel.setText("Passwords don't match");
        } else {
            errorLabel.setText(" ");
            System.out.println("Sign up OK → 触发 UseCase");
            // 这里你可以触发注册 UseCase
        }
    }

    /** Cancel clears fields */
    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        errorLabel.setText(" ");
    }
}
