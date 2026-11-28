package interface_adapter.login;

import interface_adapter.register.RegisterViewModel;
import use_case.login.LoginOutputBoundary;
import interface_adapter.ViewManagerModel;

public class LoginPresenter implements LoginOutputBoundary {
    private final ViewManagerModel viewManagerModel;
    private final RegisterViewModel registerViewModel;
    private final LoginViewModel loginViewModel;

    public LoginPresenter(ViewManagerModel viewManagerModel, RegisterViewModel registerViewModel,  LoginViewModel loginViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.registerViewModel = registerViewModel;
        this.loginViewModel = loginViewModel;
    }

    public void switchToRegisterView(){
        viewManagerModel.setState(registerViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();
        loginViewModel.resetState();
    }
}
