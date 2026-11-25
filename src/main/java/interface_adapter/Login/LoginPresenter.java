package interface_adapter.Login;

import interface_adapter.ViewManagerModel;
import interface_adapter.LoggedIn.LoggedInState;
import interface_adapter.Search.SearchBarViewModel;
import interface_adapter.LoggedIn.LoggedInViewModel;
import user_case.Login.LoginOutputBoundary;
import user_case.Login.LoginOutputData;

public class LoginPresenter implements LoginOutputBoundary {
    private final LoginViewModel loginViewModel;
    private final LoggedInViewModel loggedInViewModel;
    private final ViewManagerModel viewManagerModel;

    public LoginPresenter(ViewManagerModel viewManagerModel,
                          LoggedInViewModel loggedInViewModel,
                          LoginViewModel loginViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.loggedInViewModel = loggedInViewModel;
        this.loginViewModel = loginViewModel;
    }
    @Override
    public void PrepareSuccessView(LoginOutputData response) {
        final LoggedInState loggedInState = loggedInViewModel.getState();
        loggedInState.setUsername(response.getUsername());
        this.loggedInViewModel.notifyStateChanged();

        this.loginViewModel.setState(new LoginState());
        this.loginViewModel.notifyStateChanged();

        this.viewManagerModel.setState(SearchBarViewModel.VIEW_NAME);
        this.viewManagerModel.notifyViewChanged();
    }
    @Override
    public void PrepareFailView(String error) {
        final LoginState loginState = loginViewModel.getState();
        loginState.setLoginError(error);
        loginViewModel.notifyStateChanged();
    }
}