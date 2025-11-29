package view;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import interface_adapter.homepage.LoginChooseViewModel;
import interface_adapter.sign_up.SignupController;
import interface_adapter.sign_up.SignupState;
import interface_adapter.sign_up.SignupViewModel;
import interface_adapter.user_login.UserLoginController;
import interface_adapter.user_login.UserLoginState;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The View for the Signup Use Case.
 */
public class CreateUserAccountView extends JPanel implements ActionListener, PropertyChangeListener {
    private final String viewName = "CreateUserAccount";

    private final SignupViewModel signupViewModel;

    private final JTextField usernameField = new JTextField(15);
    private final JPasswordField passwordField = new JPasswordField(15);
    private final JPasswordField confirmPasswordField = new JPasswordField(15);
    private final JLabel usernameErrorField = new JLabel();
    private final JLabel passwordErrorField = new JLabel();
    private final JLabel confirmPasswordErrorField = new JLabel();

    private final JButton signUpButton;
    private final JButton cancelButton;
    private final JButton goBackButton;
    private SignupController signupController = null;

    private String userType = "user";
    private JLabel titleLabel;

    public CreateUserAccountView(SignupViewModel signupViewModel) {
        this.signupViewModel  = signupViewModel;
        this.signupViewModel.addPropertyChangeListener(this);

        this.userType = userType;
        this.titleLabel = new JLabel("Create User Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(new Color(60, 60, 75));

        // ====== 布局改动 ======
        setBackground(new Color(0xF5F7FB));
        setLayout(new GridBagLayout()); // 居中

        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(360, 320));
        // ====== end ======

        // put LabelTextPanel into card
        final LabelTextPanel usernameInfo = new LabelTextPanel(
                new JLabel("Username"), usernameField);
        final LabelTextPanel passwordInfo = new LabelTextPanel(
                new JLabel("Password"), passwordField);
        final LabelTextPanel confirmPasswordInfo = new LabelTextPanel(
                new JLabel("Confirm"), confirmPasswordField);

        UIFactory.styleTextField(usernameField);
        UIFactory.styleTextField(passwordField);
        UIFactory.styleTextField(confirmPasswordField);
        final JPanel buttons = new JPanel();
        buttons.setOpaque(false); 

        signUpButton = UIFactory.createPrimaryButton("Sign up");
        cancelButton = UIFactory.createSecondaryButton("Cancel");
        goBackButton = UIFactory.createPrimaryButton("Go Back");
        buttons.add(signUpButton);
        buttons.add(cancelButton);
        buttons.add(goBackButton);

        card.add(Box.createVerticalStrut(15));
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(20));

        card.add(usernameInfo);
        card.add(usernameErrorField);
        card.add(Box.createVerticalStrut(15));

        card.add(passwordInfo);
        card.add(passwordErrorField);
        card.add(Box.createVerticalStrut(15));

        card.add(confirmPasswordInfo);
        card.add(confirmPasswordErrorField);
        card.add(Box.createVerticalStrut(20));

        card.add(buttons);
        card.add(Box.createVerticalStrut(15));

        add(card);

        signUpButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(signUpButton)) {
                            final SignupState currentState = signupViewModel.getState();

                            signupController.execute(
                                    currentState.getUsername(),
                                    currentState.getPassword(),
                                    currentState.getRepeatPassword()
                            );
                        }
                    }
                }
        );

        cancelButton.addActionListener(e -> {
            if (signupController != null) {
                signupController.goBack();
            }
        });

        usernameField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final SignupState currentState = signupViewModel.getState();
                currentState.setUsername(usernameField.getText());
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

        passwordField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final SignupState currentState = signupViewModel.getState();
                currentState.setPassword(new String(passwordField.getPassword()));
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
        // test same password
        confirmPasswordField.getDocument().addDocumentListener(new DocumentListener() {
            private void documentListenerHelper() {
                final SignupState currentState = signupViewModel.getState();
                currentState.setRepeatPassword(new String(confirmPasswordField.getPassword()));
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
    }

    public void actionPerformed(ActionEvent evt) {
        System.out.println("Click " + evt.getActionCommand());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final SignupState state = (SignupState) evt.getNewValue();
        setFields(state);
        usernameErrorField.setText(state.getUsernameError());
    }

    private void setFields(SignupState state) {
        usernameField.setText(state.getUsername());
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

    public String getViewName() {
        return viewName;
    }

    public void setSignupController(SignupController signupController) {
        this.signupController = signupController;
    }

    /** Cancel clears fields */
    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        usernameErrorField.setText(" ");
        passwordErrorField.setText(" ");
        confirmPasswordErrorField.setText(" ");
    }

}
