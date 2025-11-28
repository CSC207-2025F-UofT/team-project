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
    private final JTextField usernameInputField = new JTextField(15);
    private final JLabel usernameErrorField = new JLabel();

    private final JPasswordField passwordInputField = new JPasswordField(15);
    private final JLabel passwordErrorField = new JLabel();

    private final JPasswordField repeatPasswordInputField = new JPasswordField(15);
    private final JLabel repeatPasswordErrorField = new JLabel();

    private CreateAccountController createAccountController = null;

    private final JButton goBack;
    private final JButton createAccountButton;

    public CreateAccountView(CreateAccountViewModel createAccountViewModel) {
        this.createAccountViewModel = createAccountViewModel;
        createAccountViewModel.addPropertyChangeListener(this);

        final JLabel title = new JLabel(CreateAccountViewModel.TITLE_LABEL);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));

        final LabelTextPanel usernameInfo = new LabelTextPanel(
                new JLabel(createAccountViewModel.USERNAME_LABEL), usernameInputField);

        final LabelTextPanel passwordInfo = new LabelTextPanel(
                new JLabel(createAccountViewModel.PASSWORD_LABEL), passwordInputField);

        final LabelTextPanel repeatPasswordInfo = new LabelTextPanel(
                new JLabel(createAccountViewModel.REPEAT_PASSWORD_LABEL), repeatPasswordInputField);

        usernameErrorField.setForeground(Color.RED);
        passwordErrorField.setForeground(Color.RED);
        repeatPasswordErrorField.setForeground(Color.RED);

        final JPanel buttons = new JPanel();
        goBack = new JButton(createAccountViewModel.GO_BACK_BUTTON_LABEL);
        createAccountButton = new  JButton(createAccountViewModel.SIGNUP_BUTTON_LABEL);
        buttons.add(goBack);
        buttons.add(createAccountButton);

        createAccountButton.addActionListener(this);
        goBack.addActionListener(this);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(Box.createVerticalStrut(10));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setMaximumSize(new Dimension(Integer.MAX_VALUE, title.getPreferredSize().height));
        this.add(title);
        this.add(Box.createVerticalStrut(10));

        this.add(usernameInfo);
        this.add(usernameErrorField);

        this.add(passwordInfo);
        this.add(passwordErrorField);

        this.add(repeatPasswordInfo);
        this.add(repeatPasswordErrorField);

        this.add(Box.createVerticalGlue());
        this.add(buttons);
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

