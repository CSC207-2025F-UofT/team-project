package interface_adapter.user_login;

import interface_adapter.ViewManagerModel;
import interface_adapter.homepage.HomepageState;
import interface_adapter.homepage.HomepageViewModel;
import use_case.user_login.UserLoginOutputBoundary;
import use_case.user_login.UserLoginOutputData;

public class UserLoginPresenter implements UserLoginOutputBoundary {

    private final UserLoginViewModel userLoginViewModel;
    private final HomepageViewModel homepageViewModel;
    private final ViewManagerModel viewManagerModel;

    public UserLoginPresenter(ViewManagerModel viewManagerModel,
                              HomepageViewModel homepageViewModel,
                              UserLoginViewModel userLoginViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.homepageViewModel = homepageViewModel;
        this.userLoginViewModel = userLoginViewModel;
    }

    @Override
    public void prepareSuccessView(UserLoginOutputData response) {
        // On success, update the homepageViewModel state 	
        final HomepageState homepageState = homepageViewModel.getState();
        homepageState.setUsername(response.getUsername());
        this.homepageViewModel.firePropertyChange();

        // and clear everything from the LoginViewModel's state
        userLoginViewModel.setState(new UserLoginState());

        // switch to the logged in view
        this.viewManagerModel.setState(homepageViewModel.getViewName());
        this.viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        final UserLoginState loginState = userLoginViewModel.getState();
        loginState.setLoginError(error);
        userLoginViewModel.firePropertyChange();
    }

    //for admin the successView is applicationManagement
}
