package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.login.LogInController;
import interface_adapter.login.LogInState;
import interface_adapter.login.LogInViewModel;
import interface_adapter.register.RegisterViewModel;

import javax.swing.JPasswordField;
import javax.swing.JTextField;

public final class LogInView extends AccountForm {
    private final LogInController controller;
    private final ViewManagerModel viewManager;
    private final JTextField email;
    private final JPasswordField password;

    public LogInView(LogInViewModel viewModel, LogInController controller, ViewManagerModel viewManager) {
        super("Log In");
        this.controller = controller;
        this.viewManager = viewManager;
        email = addTextField("Email", 'E');
        password = addPasswordField("Password", 'P');
        addActions("Log In", "Create an account");
        viewModel.addPropertyChangeListener(event ->
                SwingThreads.onEdt(() -> render((LogInState) event.getNewValue())));
    }

    @Override
    protected void submit() {
        final String submittedEmail = email.getText();
        final String submittedPassword = takePassword(password);
        startAction("Logging in…");
        SwingThreads.inBackground(() -> controller.logIn(submittedEmail, submittedPassword),
                () -> showFormError("Something went wrong while logging in. Please try again.", password),
                this::finishAction);
    }

    @Override
    protected void navigate() {
        viewManager.setState(RegisterViewModel.VIEW_NAME);
    }

    private void render(LogInState state) {
        clearMessages();
        if (state.error() != null) {
            showFormError(state.error(), password);
        } else if (state.notice() != null) {
            showNotice(state.notice());
        }
    }
}
