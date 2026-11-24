package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.login.LogInViewModel;
import interface_adapter.register.RegisterController;
import interface_adapter.register.RegisterState;
import interface_adapter.register.RegisterViewModel;

import javax.swing.JPasswordField;
import javax.swing.JTextField;

public final class RegisterView extends AccountForm {
    private final RegisterController controller;
    private final ViewManagerModel viewManager;
    private final JTextField email;
    private final JTextField username;
    private final JTextField displayName;
    private final JPasswordField password;
    private final JPasswordField confirmation;

    public RegisterView(RegisterViewModel viewModel, RegisterController controller, ViewManagerModel viewManager) {
        super("Register");
        this.controller = controller;
        this.viewManager = viewManager;
        email = addTextField("Email", 'E');
        username = addTextField("Username", 'U');
        displayName = addTextField("Display name", 'D');
        password = addPasswordField("Password", 'P');
        confirmation = addPasswordField("Confirm password", 'C');
        addActions("Register", "Back to Log In");
        viewModel.addPropertyChangeListener(event ->
                SwingThreads.onEdt(() -> render((RegisterState) event.getNewValue())));
    }

    @Override
    protected void submit() {
        final String submittedEmail = email.getText();
        final String submittedUsername = username.getText();
        final String submittedDisplayName = displayName.getText();
        final String submittedPassword = takePassword(password);
        final String submittedConfirmation = takePassword(confirmation);
        startAction("Registering…");
        SwingThreads.inBackground(() -> controller.register(submittedEmail, submittedUsername, submittedDisplayName,
                        submittedPassword, submittedConfirmation),
                () -> showFormError("Registration was interrupted. Try logging in before registering again.", password),
                this::finishAction);
    }

    @Override
    protected void navigate() {
        viewManager.setState(LogInViewModel.VIEW_NAME);
    }

    private void render(RegisterState state) {
        clearMessages();
        if (state.error() == null) {
            return;
        }
        switch (state.errorField()) {
            case EMAIL -> showFieldError(email, state.error());
            case USERNAME -> showFieldError(username, state.error());
            case DISPLAY_NAME -> showFieldError(displayName, state.error());
            case PASSWORD -> showFieldError(password, state.error());
            case CONFIRM_PASSWORD -> showFieldError(confirmation, state.error());
            case FORM -> showFormError(state.error(), password);
        }
    }
}
