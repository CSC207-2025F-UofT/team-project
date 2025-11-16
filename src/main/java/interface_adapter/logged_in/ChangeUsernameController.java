package interface_adapter.logged_in;

import use_case.change_username.ChangeUsernameInputBoundary;
import use_case.change_username.ChangeUsernameInputData;

public class ChangeUsernameController {
    final ChangeUsernameInputBoundary changeUsernameUseCaseInteractor;

    public ChangeUsernameController(ChangeUsernameInputBoundary changeUsernameUseCaseInteractor) {
        this.changeUsernameUseCaseInteractor = changeUsernameUseCaseInteractor;
    }

    public void execute(String currentUsername, String newUsername) {
        ChangeUsernameInputData changeUsernameInputData = new ChangeUsernameInputData(
                currentUsername,
                newUsername
        );
        changeUsernameUseCaseInteractor.execute(changeUsernameInputData);
    }
}