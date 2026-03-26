package view;

import interface_adapter.createAccount.CreateAccountController;
import interface_adapter.createAccount.CreateAccountState;
import interface_adapter.createAccount.CreateAccountViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class CreateAccountView extends JPanel implements ActionListener, PropertyChangeListener {
    private final String viewName = "create account";

    private final CreateAccountViewModel createAccountViewModel;
    private CreateAccountController createAccountController = null;

    private final JTextField usernameInputField = new JTextField(15);
    private final JLabel usernameErrorField = new JLabel();

    private final JPasswordField passwordInputField = new JPasswordField(15);
    private final JLabel passwordErrorField = new JLabel();

    private final JPasswordField repeatPasswordInputField = new JPasswordField(15);
    private final JLabel repeatPasswordErrorField = new JLabel();

    private final JButton goBack;
    private final JButton createAccountButton;

    public CreateAccountView(CreateAccountViewModel createAccountViewModel) {
        this.createAccountViewModel = createAccountViewModel;
        createAccountViewModel.addPropertyChangeListener(this);

        // TITLE
        JLabel title = new JLabel(CreateAccountViewModel.TITLE_LABEL);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));

        // USERNAME PANEL
        JLabel usernameLabel = new JLabel(CreateAccountViewModel.USERNAME_LABEL);
        JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameInputField);

        // ERROR LABELS
        usernameErrorField.setForeground(Color.RED);
        usernameErrorField.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameErrorField.setHorizontalAlignment(SwingConstants.CENTER);

        // PASSWORD PANEL
        JLabel passwordLabel = new JLabel(CreateAccountViewModel.PASSWORD_LABEL);
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordInputField);

        passwordErrorField.setForeground(Color.RED);
        passwordErrorField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordErrorField.setHorizontalAlignment(SwingConstants.CENTER);

        // REPEAT PASSWORD PANEL
        JLabel repeatPasswordLabel = new JLabel(CreateAccountViewModel.REPEAT_PASSWORD_LABEL);
        JPanel repeatPasswordPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        repeatPasswordPanel.add(repeatPasswordLabel);
        repeatPasswordPanel.add(repeatPasswordInputField);

        repeatPasswordErrorField.setForeground(Color.RED);
        repeatPasswordErrorField.setAlignmentX(Component.CENTER_ALIGNMENT);
        repeatPasswordErrorField.setHorizontalAlignment(SwingConstants.CENTER);

        // BUTTON PANEL
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        goBack = new JButton(CreateAccountViewModel.GO_BACK_BUTTON_LABEL);
        createAccountButton = new JButton(CreateAccountViewModel.SIGNUP_BUTTON_LABEL);
        buttonsPanel.add(createAccountButton);
        buttonsPanel.add(goBack);

        // IMPORTANT: Center panels in BoxLayout
        usernamePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        repeatPasswordPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // LOCK HEIGHT (so BoxLayout doesn't stretch panels)
        lockHeight(usernamePanel);
        lockHeight(passwordPanel);
        lockHeight(repeatPasswordPanel);
        lockHeight(buttonsPanel);
        lockHeight(usernameErrorField);
        lockHeight(passwordErrorField);
        lockHeight(repeatPasswordErrorField);

        // ADD COMPONENTS TO VIEW
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(Box.createVerticalStrut(10));
        this.add(title);
        this.add(Box.createVerticalStrut(15));

        this.add(usernamePanel);
        this.add(usernameErrorField);
        this.add(Box.createVerticalStrut(10));

        this.add(passwordPanel);
        this.add(passwordErrorField);
        this.add(Box.createVerticalStrut(10));

        this.add(repeatPasswordPanel);
        this.add(repeatPasswordErrorField);
        this.add(Box.createVerticalStrut(10));

        this.add(buttonsPanel);
        this.add(Box.createVerticalGlue());


        createAccountButton.addActionListener(this);
        goBack.addActionListener(this);
    }

    private void lockHeight(JComponent c) {
        Dimension pref = c.getPreferredSize();
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, pref.height));
    }

    public void setCreateController(CreateAccountController controller) {
        this.createAccountController = controller;
    }

    public String getViewName() {return viewName;}

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        // create account button
        if (source == createAccountButton) {
            // get value and update the state
            CreateAccountState state = createAccountViewModel.getState();
            state.setUsername(usernameInputField.getText());
            state.setPassword(String.valueOf(passwordInputField.getPassword()));
            state.setRepeatPassword(String.valueOf(repeatPasswordInputField.getPassword()));
            createAccountViewModel.setState(state);

            //  Controller
            createAccountController.execute(
                    state.getUsername(),
                    state.getPassword(),
                    state.getRepeatPassword()
            );
        } else if (source == goBack) {
            createAccountController.switchToHomePage();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!"state".equals(evt.getPropertyName())) {
            return;
        }

        CreateAccountState state = (CreateAccountState) evt.getNewValue();

        // put the data of state
        usernameInputField.setText(state.getUsername());
        passwordInputField.setText(state.getPassword());
        repeatPasswordInputField.setText(state.getRepeatPassword());

        usernameErrorField.setText(state.getUsernameError());
        passwordErrorField.setText(state.getPasswordError());
        repeatPasswordErrorField.setText(state.getRepeatPasswordError());
    }

}

