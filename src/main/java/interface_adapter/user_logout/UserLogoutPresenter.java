package interface_adapter.user_logout;

import interface_adapter.ViewManagerModel;
import interface_adapter.homepage.HomepageState;
import interface_adapter.homepage.HomepageViewModel;
import interface_adapter.user_login.UserLoginState;
import interface_adapter.user_login.UserLoginViewModel;
import service.Frontend;
import use_case.user_logout.UserLogoutOutputBoundary;
import use_case.user_logout.UserLogoutOutputData;

/**
 * The Presenter for the Logout Use Case.
 */
public class UserLogoutPresenter implements UserLogoutOutputBoundary {

    private ViewManagerModel viewManagerModel;
    private UserLoginViewModel userLoginViewModel;
	private final Frontend frontend;

    public UserLogoutPresenter(ViewManagerModel viewManagerModel,
            UserLoginViewModel userLoginViewModel,
            Frontend frontend) {
        this.viewManagerModel = viewManagerModel;
        this.frontend = frontend;
        this.userLoginViewModel = userLoginViewModel;
    }

    @Override
    public void prepareSuccessView(UserLogoutOutputData response) {
        // We need to switch to the login view, which should have
        // an empty username and password.

        // We also need to set the username in the LoggedInState to
        // the empty string.
    	
        // 清除登录信息
//        final UserLoginState loginState = userLoginViewModel.getState();
//        loginState.setUsername(response.getUsername());
        userLoginViewModel.setState(new UserLoginState());
        userLoginViewModel.firePropertyChange();
        
        // 回到 LoginChoose 页面
//        frontend.showLoginChoose();

        // This code tells the View Manager to switch to the LoginView.
        this.viewManagerModel.setState("loginChoose");
        this.viewManagerModel.setWindowTitle("Pet Adoption System");
        this.viewManagerModel.firePropertyChange();
    }
    

}
