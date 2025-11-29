package use_case.login;

import entity.User;
import entity.UserFactory;

/**
 * The Login Interactor.
 */
public class LoginInteractor implements LoginInputBoundary {
    private final LoginUserDataAccessInterface userDataAccessObject;
    private final LoginOutputBoundary loginPresenter;

    public LoginInteractor(LoginUserDataAccessInterface userDataAccessInterface,
                           LoginOutputBoundary loginOutputBoundary) {
        this.userDataAccessObject = userDataAccessInterface;
        this.loginPresenter = loginOutputBoundary;
    }

    @Override
    public void execute(LoginInputData loginInputData) {
        final String username = loginInputData.getUsername();
        final String password = loginInputData.getPassword();
        if (!userDataAccessObject.existsByName(username)) {
            loginPresenter.prepareFailView(username + ": Account does not exist.");
        }
        else {
            final String pwdHash = userDataAccessObject.get(username).getPasswordHash();
            System.out.println(pwdHash);
            System.out.println(UserFactory.hashPasswordSHA256(password));
            System.out.println(UserFactory.hashPasswordSHA256(password));
            System.out.println(UserFactory.hashPasswordSHA256(password));
            if (!pwdHash.equals(UserFactory.hashPasswordSHA256(password))) {
                loginPresenter.prepareFailView("Incorrect password for \"" + username + "\".");
            }
            else {

                final User user = userDataAccessObject.get(loginInputData.getUsername());

                userDataAccessObject.setCurrentUsername(username);

                final LoginOutputData loginOutputData = new LoginOutputData(user.getName());
                loginPresenter.prepareSuccessView(loginOutputData);
            }
        }
    }
}
