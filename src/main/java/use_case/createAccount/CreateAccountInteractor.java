package use_case.createAccount;

import entity.user.User;
import entity.user.UserFactory;

public class CreateAccountInteractor implements CreateAccountInputBoundary {

    private final CreateAccountUserDataAccessInterface userDataAccessObject;
    private final UserFactory userFactory;
    private final CreateAccountOutputBoundary presenter;

    public CreateAccountInteractor(CreateAccountOutputBoundary presenter, CreateAccountUserDataAccessInterface createAccountUserDataAccessInterface, UserFactory userFactory) {
        this.presenter = presenter;
        this.userDataAccessObject = createAccountUserDataAccessInterface;
        this.userFactory = userFactory;
    }

    @Override
    public void execute(CreateAccountInputData inputData) {
        String username = inputData.getUsername();
        String password = inputData.getPassword();
        String repeatPassword = inputData.getRepeatPassword();

        //** CHECK IF THE USERNAME IS ALREADY TAKEN AND PASSWORDS MATCH **//
        if (userDataAccessObject.existsByName(username)) {
            presenter.prepareFailView("Username already exists",
                    null, null);
            return;
        } else if (!password.equals(repeatPassword)) {
            presenter.prepareFailView(null,
                    "Passwords do not match",
                    "Passwords do not match");
            return;
        }

        User user = userFactory.create(username, password,
                new java.util.ArrayList<>()); // or some token

        userDataAccessObject.save(user);

        CreateAccountOutputData outputData = new CreateAccountOutputData(username);
        presenter.prepareSuccessView(outputData);
    }

    @Override
    public void switchToHomePage() {
        presenter.switchToHomePage();
    }
}
