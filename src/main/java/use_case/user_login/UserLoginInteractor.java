package use_case.user_login;

import entity.User;

public class UserLoginInteractor implements UserLoginInputBoundary {
    private final UserLoginUserDataAccessInterface userDataAccessObject;
    private final UserLoginOutputBoundary userLoginPresenter;

    public UserLoginInteractor(UserLoginUserDataAccessInterface userDataAccessInterface,
                           UserLoginOutputBoundary loginOutputBoundary) {
        this.userDataAccessObject = userDataAccessInterface;
        this.userLoginPresenter = loginOutputBoundary;
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
            final String pwd = userDataAccessObject.get(username).getPassword();
            if (!password.equals(pwd)) {
                userLoginPresenter.prepareFailView("Incorrect password for \"" + username + "\".");
            }
            else {

                final User user = userDataAccessObject.get(loginInputData.getUsername());

                userDataAccessObject.setCurrentUsername(username);

                final UserLoginOutputData userLoginOutputData = new UserLoginOutputData(user.getName(), user.getUserType());
                userLoginPresenter.prepareSuccessView(userLoginOutputData);
            }
        }
    }

	@Override
	public void goBack() {
		userLoginPresenter.prepareGoBackView("loginChoose");		
	}
}
