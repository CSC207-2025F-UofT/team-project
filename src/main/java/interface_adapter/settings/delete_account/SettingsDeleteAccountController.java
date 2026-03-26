package interface_adapter.settings.delete_account;

import use_case.deleteAccount.DeleteAccountInputBoundary;
import use_case.deleteAccount.DeleteAccountInputData;

public class SettingsDeleteAccountController {
    private final DeleteAccountInputBoundary deleteAccountInputBoundary;

    public SettingsDeleteAccountController(DeleteAccountInputBoundary deleteAccountInputBoundary) {
        this.deleteAccountInputBoundary = deleteAccountInputBoundary;
    }


    public void deleteAccount(String currentUserName) {
        final DeleteAccountInputData deleteAccountInputData = new DeleteAccountInputData(currentUserName);
        deleteAccountInputBoundary.execute(deleteAccountInputData);
    }
}
