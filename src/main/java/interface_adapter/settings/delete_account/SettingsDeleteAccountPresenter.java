package interface_adapter.settings.delete_account;

import interface_adapter.ViewManagerModel;
import use_case.deleteAccount.DeleteAccountOutputBoundary;
import use_case.deleteAccount.DeleteAccountOutputData;

import javax.swing.*;

public class SettingsDeleteAccountPresenter implements DeleteAccountOutputBoundary {

    public SettingsDeleteAccountPresenter() {}

    @Override
    public void prepareSuccessView(DeleteAccountOutputData outputData) {
        // popup
        JOptionPane.showMessageDialog(null, "Account deleted: " + outputData.getUsername());

    }
}
