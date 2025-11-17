package use_case.change_username;

import entity.UserFactory;

public class ChangeUsernameInteractor implements ChangeUsernameInputBoundary {
    private final ChangeUsernameUserDataAccessInterface userDataAccessObject;
    private final ChangeUsernameOutputBoundary userPresenter;
    private final UserFactory userFactory;

    public ChangeUsernameInteractor(ChangeUsernameUserDataAccessInterface userDataAccessObject,
                                    ChangeUsernameOutputBoundary userPresenter,
                                    UserFactory userFactory) {
        this.userDataAccessObject = userDataAccessObject;
        this.userPresenter = userPresenter;
        this.userFactory = userFactory;
    }

    @Override
    public void execute(ChangeUsernameInputData inputData) {
        String newUsername = inputData.getNewUsername();
        String currentUsername = inputData.getCurrentUsername();

        if (newUsername.isEmpty()) {
            userPresenter.prepareFailView("New username cannot be empty.");
        } else if (userDataAccessObject.existsByName(newUsername)) {
            userPresenter.prepareFailView("Username '" + newUsername + "' is already taken.");
        } else {
            userDataAccessObject.changeUsername(currentUsername, newUsername);

            ChangeUsernameOutputData outputData = new ChangeUsernameOutputData(newUsername);
            userPresenter.prepareSuccessView(outputData);
        }
    }
}