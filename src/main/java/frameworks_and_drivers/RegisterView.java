package frameworks_and_drivers;
import javax.swing.*;
import java.awt.*;

import interface_adapter.register.RegisterController;
import interface_adapter.register.RegisterViewModel;
import interface_adapter.register.RegisterViewModelListener;
import interface_adapter.register.RegisterState;

public class RegisterView extends JPanel implements RegisterViewModelListener {

    private final RegisterViewModel registerViewModel;

    private final JButton registerButton = new JButton("Register");
    private final JButton backButton = new JButton("Back");

    private final JLabel errorLabel = new JLabel("Error");

    private final JTextField usernameField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JPasswordField confirmPasswordField = new JPasswordField(20);
    private final JTextField emailField = new JTextField(20);

    private RegisterController registerController;

    public RegisterView(RegisterViewModel registerViewModel){
        this.registerViewModel = registerViewModel;

        JLabel title = new JLabel("Register");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(BorderFactory.createEmptyBorder(100, 0, 200, 0));
        this.add(title);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.add(new LabelTextPanel(new JLabel("Email: "), emailField));
        this.add(new LabelTextPanel(new JLabel("Username "), usernameField));
        this.add(new LabelTextPanel(new JLabel("Password: "), passwordField));
        this.add(new LabelTextPanel(new JLabel("Confirm Password: "), confirmPasswordField));
        this.add(Box.createVerticalStrut(50));
        this.add(registerButton);
        this.add(Box.createVerticalStrut(50));
        this.add(backButton);
        this.add(Box.createVerticalStrut(50));

        errorLabel.setForeground(Color.RED);
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorLabel.setVisible(false);

        this.add(errorLabel);

        Component[] components = this.getComponents();

        for (int i = 1; i < components.length; i++) {
            components[i].setMaximumSize(components[i].getPreferredSize());
            ((JComponent) components[i]).setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        initializeListeners();
    }
    private void initializeListeners(){
        registerViewModel.addListener(this);

        registerButton.addActionListener(e -> {
            registerController.execute(emailField.getText(), usernameField.getText(), passwordField.getPassword(),
                    confirmPasswordField.getPassword());
        });

        backButton.addActionListener(e -> {
            registerController.switchToLoginView();
        });
    }

    @Override
    public void stateChanged(){
        RegisterState registerState = registerViewModel.getState();
        errorLabel.setText(registerState.getErrorMessage());
        errorLabel.setVisible(!registerState.getErrorMessage().isEmpty());
    }

    public void setRegisterController(RegisterController registerController){
        this.registerController = registerController;
    }
}
