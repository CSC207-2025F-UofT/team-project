package UseCases.SignUp;

import Entities.User;
import Entities.UserProfile;
import DataAccess.UserDataAccessInterface;

public class SignUpInteractor implements SignUpInputBoundary {
    private final UserDataAccessInterface userDataAccessObject;
    private final SignUpOutputBoundary userPresenter;
    private User user;
    private UserProfile profile;

    public SignUpInteractor(UserDataAccessInterface userDataAccessObject,
                            SignUpOutputBoundary signUpOutputBoundary, User user, UserProfile profile) {
        this.userDataAccessObject = userDataAccessObject;
        this.userPresenter = signUpOutputBoundary;
        this.user = user;
        this.profile = profile;
    }

    @Override
    public void execute(SignUpInputData signUpInputData) {
        if (userDataAccessObject.doesUserExist(signUpInputData.getUsername())) {
            userPresenter.prepareFailView("Username already exists.");
        }
        else if ("".equals(signUpInputData.getUsername())) {
            userPresenter.prepareFailView("Username is required");
        }

        if (!signUpInputData.getPassword().equals(signUpInputData.getPasswordConfirm())) {
            userPresenter.prepareFailView("Passwords don't match.");
        }
        if ("".equals(signUpInputData.getPassword())) {
            userPresenter.prepareFailView("Password is required.");
        }
        if ("".equals(signUpInputData.getFirstName())) {
            userPresenter.prepareFailView("First name is required.");
        }
        if ("".equals(signUpInputData.getLastName())) {
            userPresenter.prepareFailView("Last name is required.");
        }

        if (signUpInputData.getAge() == null) {
            userPresenter.prepareFailView("Age is required.");
        }
        else if (!(signUpInputData.getAge() instanceof Integer) || signUpInputData.getAge() < 0) {
            userPresenter.prepareFailView("Enter a valid age.");
        }

        if ("".equals(signUpInputData.getOccupation())) {
            userPresenter.prepareFailView("Occupation is required.");
        }
        
        this.profile = UserProfile.UserProfileFactory(signUpInputData.getFirstName(),
        signUpInputData.getLastName(), signUpInputData.getAge(), signUpInputData.getGender(),
        signUpInputData.getOccupation(), signUpInputData.getBiography());
        this.user = User.UserFactory(signUpInputData.getUsername(), signUpInputData.getPassword(), 
        profile);
        userDataAccessObject.create(user);

        final SignUpOutputData signUpOutputData = new SignUpOutputData(user.getUsername());
        userPresenter.prepareSuccessView(signUpOutputData);
    }

    @Override
    public void switchToLoginView() {
        userPresenter.switchToLoginView();
    }
}
