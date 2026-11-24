package interface_adapter.settings;

import use_case.change_password.ChangePasswordOutputBoundary;
import use_case.change_password.ChangePasswordOutputData;

/**
 * The Presenter for the Change Password Use Case.
 */
public class ChangePasswordPresenter implements ChangePasswordOutputBoundary {

    private final SettingsViewModel settingsViewModel;

    public ChangePasswordPresenter(SettingsViewModel settingsViewModel) {
        this.settingsViewModel = settingsViewModel;
    }

    @Override
    public void prepareSuccessView(ChangePasswordOutputData outputData) {
        String message = "Password changed for @" + outputData.getUsername() + ".";
        settingsViewModel.setState(new SettingsState(null, message));
    }

    @Override
    public void prepareFailView(String errorMessage) {
        settingsViewModel.setState(new SettingsState(errorMessage, null));
    }
}
