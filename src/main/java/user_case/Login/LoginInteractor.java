package user_case.Login;
import entity.User;

public class LoginInteractor implements LoginInputBoundary{
    private final LoginUserDataAccessInterface userDataAccessObject;
    private final LoginOutputBoundary loginPresenter;

    public LoginInteractor(LoginUserDataAccessInterface userDataAccessInterface,
                           LoginOutputBoundary loginOutputBoundary){
        this.userDataAccessObject = userDataAccessInterface;
        this.loginPresenter = loginOutputBoundary;
    }
    @Override
    public void execute(LoginInputData loginInputData){
        final String username = loginInputData.getUsername();
        final String password = loginInputData.getPassword();

        if(!userDataAccessObject.existByName(username)){
            loginPresenter.PrepareFailView(username + "Account doesn't exist");
        } else{
            final String pass = userDataAccessObject.get(username).getPassword();
            if (!userDataAccessObject.checkPassword(username, password)) {
                loginPresenter.PrepareFailView("Incorrect password. Try again!");
            } else{
                final User user = userDataAccessObject.get(loginInputData.getUsername());
                userDataAccessObject.setCurrentUsername(username);

                final LoginOutputData loginOutputData = new LoginOutputData(user.getName());
                loginPresenter.PrepareSuccessView(loginOutputData);
            }
        }
    }

}
