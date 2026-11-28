package use_case.login;

public class LoginInteractor implements LoginInputBoundary{
    private final LoginOutputBoundary loginPresenter;

    public LoginInteractor(LoginOutputBoundary loginPresenter) {
        this.loginPresenter = loginPresenter;
    }

    public void execute(LoginInputData loginInputData){}

    public void switchToRegisterView(){
        loginPresenter.switchToRegisterView();
    }
}
