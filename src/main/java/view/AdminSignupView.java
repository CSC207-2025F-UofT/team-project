package view;

import interface_adapter.admin_signup.AdminSignupController;
import interface_adapter.admin_signup.AdminSignupState;
import interface_adapter.admin_signup.AdminSignupViewModel;

import interface_adapter.admin_signup.AdminSignupViewModelListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The View for the Admin Signup Use Case.
 */
public class AdminSignupView extends JPanel implements ActionListener, AdminSignupViewModelListener {

    private final AdminSignupViewModel signupViewModel;
    private final JTextField usernameInputField = new JTextField(15);
    private final JTextField idInputField = new JTextField(15);
    private final JTextField emailInputField = new JTextField(15);
    private final JPasswordField passwordInputField = new JPasswordField(15);
    private final JPasswordField repeatPasswordInputField = new JPasswordField(15);
    private final JTextField inviteCodeInputField = new JTextField(15);
    private AdminSignupController signupController = null;

    private final JButton signUp;
    private final JButton cancel;
    private final JButton toLogin;

    public AdminSignupView(AdminSignupViewModel signupViewModel) {
        this.signupViewModel = signupViewModel;
        signupViewModel.addListener(this);

        final JLabel title = new JLabel(AdminSignupViewModel.TITLE_LABEL);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JPanel usernameInfo = row(
                AdminSignupViewModel.USERNAME_LABEL, usernameInputField);
        final JPanel idInfo = row(
                AdminSignupViewModel.ID_LABEL, idInputField);
        final JPanel emailInfo = row(
                AdminSignupViewModel.EMAIL_LABEL, emailInputField);
        final JPanel passwordInfo = row(
                AdminSignupViewModel.PASSWORD_LABEL, passwordInputField);
        final JPanel repeatPasswordInfo = row(
                AdminSignupViewModel.REPEAT_PASSWORD_LABEL, repeatPasswordInputField);
        final JPanel inviteCodeInfo = row(
                AdminSignupViewModel.INVITE_CODE_LABEL, inviteCodeInputField);

        final JPanel buttons = new JPanel();
        toLogin = new JButton(AdminSignupViewModel.TO_LOGIN_BUTTON_LABEL);
        buttons.add(toLogin);
        signUp = new JButton(AdminSignupViewModel.SIGNUP_BUTTON_LABEL);
        buttons.add(signUp);
        cancel = new JButton(AdminSignupViewModel.CANCEL_BUTTON_LABEL);
        buttons.add(cancel);

        signUp.addActionListener(
                // This creates an anonymous subclass of ActionListener and instantiates it.
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(signUp)) {
                            final AdminSignupState currentState = signupViewModel.getState();

                            signupController.execute(
                                    currentState.getUsername(),
                                    currentState.getId(),
                                    currentState.getEmail(),
                                    currentState.getPassword(),
                                    currentState.getRepeatPassword(),
                                    currentState.getInviteCode()
                            );
                        }
                    }
                }
        );

        toLogin.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        signupController.switchToLoginView();
                    }
                }
        );

        cancel.addActionListener(this);

        addUsernameListener();
        addIdListener();
        addEmailListener();
        addPasswordListener();
        addRepeatPasswordListener();
        addInviteCodeListener();

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.add(title);
        this.add(usernameInfo);
        this.add(idInfo);
        this.add(emailInfo);
        this.add(passwordInfo);
        this.add(repeatPasswordInfo);
        this.add(inviteCodeInfo);
        this.add(buttons);
    }

    private static JPanel row(String label, Component field) {
        final JPanel panel = new JPanel();
        final JLabel jLabel = new JLabel(label);
        jLabel.setPreferredSize(new Dimension(160, 20));
        panel.add(jLabel);
        panel.add(field);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }

    private void addUsernameListener() {
        usernameInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final AdminSignupState currentState = signupViewModel.getState();
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
    }

    private void addIdListener() {
        idInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final AdminSignupState currentState = signupViewModel.getState();
                currentState.setId(idInputField.getText());
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

    private void addEmailListener() {
        emailInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final AdminSignupState currentState = signupViewModel.getState();
                currentState.setEmail(emailInputField.getText());
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

    private void addPasswordListener() {
        passwordInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final AdminSignupState currentState = signupViewModel.getState();
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
    }

    private void addRepeatPasswordListener() {
        repeatPasswordInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final AdminSignupState currentState = signupViewModel.getState();
                currentState.setRepeatPassword(new String(repeatPasswordInputField.getPassword()));
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

    private void addInviteCodeListener() {
        inviteCodeInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final AdminSignupState currentState = signupViewModel.getState();
                currentState.setInviteCode(inviteCodeInputField.getText());
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

    @Override
    public void actionPerformed(ActionEvent evt) {
        JOptionPane.showMessageDialog(this, "Cancel not implemented yet.");
    }

    @Override
    public void stateChanged() {
        if (signupViewModel.getState().getSignupError() != null) {
            JOptionPane.showMessageDialog(this, signupViewModel.getState().getSignupError());
        }
    }

    public void setSignupController(AdminSignupController controller) {
        this.signupController = controller;
    }
}
