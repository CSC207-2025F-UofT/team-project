package use_case.user_login;

import entity.User;
import service.Backend;

public class UserLoginInteractor implements UserLoginInputBoundary {
    private final UserLoginUserDataAccessInterface userDataAccessObject;
    private final UserLoginOutputBoundary userLoginPresenter;

    private final Backend backend;

    public UserLoginInteractor(UserLoginUserDataAccessInterface userDataAccessObject,
                               UserLoginOutputBoundary userLoginPresenter,
                               Backend backend) {
        this.userDataAccessObject = userDataAccessObject;
        this.userLoginPresenter = userLoginPresenter;
        this.backend = backend;
    }

    @Override
    public void execute(UserLoginInputData loginInputData) {
        final String userType = loginInputData.getUserType();
        final String username = loginInputData.getUsername();
        final String password = loginInputData.getPassword();

        if (!userDataAccessObject.existsByName(username)) {
            userLoginPresenter.prepareFailView(username + ": Account does not exist.");
        }
        else {
            final String loginPageUserType = userDataAccessObject.get(username).getUserType();
            if (!userType.equals(loginPageUserType)) {
                userLoginPresenter.prepareFailView(
                        "Its not " + userType + " account.");
                return;
            }
            // check password
            final String pwd = userDataAccessObject.get(username).getPassword();
            if (!password.equals(pwd)) {
                userLoginPresenter.prepareFailView("Incorrect password for \"" + username + "\".");
                return;
            }


            final User user = userDataAccessObject.get(loginInputData.getUsername());
            
            backend.setCurrentUser(user);
            userDataAccessObject.setCurrentUsername(username);

            final UserLoginOutputData userLoginOutputData = new UserLoginOutputData
                    (user.getUsername(), user.getUserType(), user);
            
            userLoginPresenter.prepareSuccessView(userLoginOutputData);

        }
    }

	@Override
	public void goBack() {
		userLoginPresenter.prepareGoBackView("loginChoose");
	}
}
