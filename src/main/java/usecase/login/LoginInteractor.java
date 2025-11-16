package usecase.login;

public class LoginInteractor implements LoginInputBoundary {

    public LoginUserDataAccessInterface loginUserDataAccessObject;
    public LoginOutputBoundary loginPresenter;

    @Override
    public void execute(LoginInputData loginInputData) {

    }
}
