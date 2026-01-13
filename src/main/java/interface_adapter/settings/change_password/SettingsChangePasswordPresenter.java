package interface_adapter.settings.change_password;

import interface_adapter.settings.SettingsViewModel;
import use_case.changePassword.ChangePasswordOutputBoundary;
import use_case.changePassword.ChangePasswordOutputData;

import javax.swing.*;

public class SettingsChangePasswordPresenter implements ChangePasswordOutputBoundary{
    private SettingsViewModel settingsViewModel;

    public SettingsChangePasswordPresenter(SettingsViewModel settingsViewModel){
        this.settingsViewModel = settingsViewModel;
    }

    @Override
    public void prepareSuccessView(ChangePasswordOutputData outputData) {
        settingsViewModel.getState().setNewPassword("");
        settingsViewModel.getState().setPasswordError("");
        settingsViewModel.firePropertyChanged("passwordError");

        // popup
        JOptionPane.showMessageDialog(null, "Account deleted: " + outputData.getUsername());
    }

    @Override
    public void prepareFailView(String error) {
        settingsViewModel.getState().setPasswordError(error);
        settingsViewModel.firePropertyChanged("passwordError");
    }

}
