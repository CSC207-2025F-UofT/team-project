package use_case.signup;

import entity.UserProfile;
import entity.UserFactory;

public class SignUpInteractor implements SignUpInputBoundary{
    private final SignUpDataAccessInterface dataAccessObject;
    private final SignUpOutputBoundary presenter;
    private final PasswordHasher hasher;
    private final UserFactory userFactory;

    public SignUpInteractor(SignUpDataAccessInterface dataAccessObject, SignUpOutputBoundary presenter,
            PasswordHasher hasher, UserFactory userFactory) {

        this.dataAccessObject = dataAccessObject;
        this.presenter = presenter;
        this.hasher = hasher;
        this.userFactory = userFactory;
    }

    @Override
    public void execute(SignUpInputData input) {

        String name = input.getUserName();
        String password = input.getPassword();

        if (name == null || name.isBlank()) {
            presenter.prepareFailureView("Username is required.");
            return;
        }

        if (password == null || password.isBlank()) {
            presenter.prepareFailureView("Password is required.");
            return;
        }

        if (!password.equals(input.getRepeatPassword())) {
            presenter.prepareFailureView("Passwords are not same");
            return;
        }

        name = name.trim();

        UserProfile profile;

        try {

            if (dataAccessObject.existUserName(name)) {
                presenter.prepareFailureView("Name already exists");
                return;
            }

            UserProfile user = userFactory.create(dataAccessObject.nextUserId(), name,
                    hasher.hash(password));
            profile = user;

            dataAccessObject.saveUserProfile(user);

        } catch (IllegalStateException ex) {
            presenter.prepareFailureView("Unable to register account. Please try again.");
            return;
        }

        SignUpOutputData signUpOutputData = new SignUpOutputData(profile.getName());

        presenter.prepareSuccessView(signUpOutputData);
    }
}
