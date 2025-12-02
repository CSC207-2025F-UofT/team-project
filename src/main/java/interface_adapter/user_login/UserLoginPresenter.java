package interface_adapter.user_login;

import interface_adapter.ViewManagerModel;
import service.Backend;
import service.Frontend;
import use_case.user_login.UserLoginOutputBoundary;
import use_case.user_login.UserLoginOutputData;

public class UserLoginPresenter implements UserLoginOutputBoundary {

    private final UserLoginViewModel userLoginViewModel;
    private final ViewManagerModel viewManagerModel;
    private final Frontend frontend;
    private final Backend backend;
    
    public UserLoginPresenter(ViewManagerModel viewManagerModel,
                              UserLoginViewModel userLoginViewModel,
                              Frontend frontend,
                              Backend backend) {
        this.viewManagerModel = viewManagerModel;
        this.userLoginViewModel = userLoginViewModel;
        this.frontend = frontend;
        this.backend = backend;
    }

    @Override
    public void prepareSuccessView(UserLoginOutputData response) {
    	System.out.println("[UserLoginPresenter] success userType = " + response.getUserType());
        // clear login state
        userLoginViewModel.setState(new UserLoginState());

        frontend.setVisible(true);
        frontend.showDashboard(response.getUserType());
        backend.setCurrentUser(response.getUser());

        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        final UserLoginState loginState = userLoginViewModel.getState();
        loginState.setLoginError(error);
        userLoginViewModel.firePropertyChange();
    }

    @Override
    public void prepareGoBackView(String viewName) {
        viewManagerModel.setState(viewName);
        viewManagerModel.setWindowTitle("Pet Adoption System");
        viewManagerModel.firePropertyChange();
    }
}
