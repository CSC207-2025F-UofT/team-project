package use_case.signIn;

import entity.user.User;

/**
 * The Login Interactor.
 */
public class SignInInteractor implements SignInInputBoundary {
    private final SignInUserDataAccessInterface userDataAccessObject;
    private final SignInOutputBoundary signInPresenter;

    public SignInInteractor(SignInOutputBoundary signInPresenter, SignInUserDataAccessInterface userDataAccessObject) {
        this.userDataAccessObject = userDataAccessObject;
        this.signInPresenter = signInPresenter;
    }

    @Override
    public void switchToHomePage() {
        signInPresenter.switchToHomePage();
    }

    @Override
    public void execute(SigninInputData signinInputData) {
        final String username = signinInputData.getUsername();
        final String password = signinInputData.getPassword();
        if (!userDataAccessObject.existsByName(username)) {
            signInPresenter.prepareFailView(username + ": Account does not exist.");
        }
        else {
            final String pwd = userDataAccessObject.get(username).getPassword();
            if (!password.equals(pwd)) {
                signInPresenter.prepareFailView("Incorrect password for \"" + username + "\".");
            }
            else {

                final User user = userDataAccessObject.get(signinInputData.getUsername());

                userDataAccessObject.setCurrentUsername(username);

                final SignInOutputData signinOutputData = new SignInOutputData(user.getName());
                signInPresenter.prepareSuccessView(signinOutputData);
            }
        }
    }
}