package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.home.HomeViewModel;
import interface_adapter.settings.ChangePasswordController;
import interface_adapter.settings.SettingsState;
import interface_adapter.settings.SettingsViewModel;

import javax.swing.JPasswordField;

/**
 * The View for Settings. For now it only has Change Password (#22).
 * Built on AccountForm, so it gets the same layout, errors and busy state as Log In.
 */
public final class SettingsView extends AccountForm {
    private final ChangePasswordController changePasswordController;
    private final ViewManagerModel viewManagerModel;

    private final JPasswordField currentPasswordField;
    private final JPasswordField newPasswordField;
    private final JPasswordField confirmPasswordField;

    public SettingsView(SettingsViewModel settingsViewModel, ChangePasswordController changePasswordController,
                        ViewManagerModel viewManagerModel) {
        super("Settings");
        this.changePasswordController = changePasswordController;
        this.viewManagerModel = viewManagerModel;

        currentPasswordField = addPasswordField("Current password", 'C');
        newPasswordField = addPasswordField("New password", 'N');
        confirmPasswordField = addPasswordField("Confirm new password", 'M');
        addActions("Change Password", "Back");

        // Redraw whenever the presenter updates the view model
        settingsViewModel.addPropertyChangeListener(event ->
                SwingThreads.onEdt(() -> render((SettingsState) event.getNewValue())));
    }

    /** Change Password button (or Enter in a field). */
    @Override
    protected void submit() {
        // Read without clearing, so a typo doesn't make you retype everything (cleared on success in render)
        final String currentPassword = new String(currentPasswordField.getPassword());
        final String newPassword = new String(newPasswordField.getPassword());
        final String confirmPassword = new String(confirmPasswordField.getPassword());

        startAction("Changing password…");

        // Off the Swing thread, since this goes over the network
        SwingThreads.inBackground(
                () -> changePasswordController.execute(currentPassword, newPassword, confirmPassword),
                () -> showFormError("Something went wrong. Please try again.", currentPasswordField),
                this::finishAction);
    }

    /** Back button. */
    @Override
    protected void navigate() {
        viewManagerModel.setState(HomeViewModel.VIEW_NAME);
    }

    private void render(SettingsState state) {
        clearMessages();

        if (state.getPasswordError() != null) {
            showFormError(state.getPasswordError(), currentPasswordField);

        } else if (state.getPasswordMessage() != null) {
            showNotice(state.getPasswordMessage());

            // Changed, so the old typed passwords aren't needed anymore
            currentPasswordField.setText("");
            newPasswordField.setText("");
            confirmPasswordField.setText("");
        }
    }
}
