package interface_adapter.user_login;

import interface_adapter.ViewManagerModel;
import service.Frontend;
import use_case.user_login.UserLoginOutputBoundary;
import use_case.user_login.UserLoginOutputData;

public class UserLoginPresenter implements UserLoginOutputBoundary {

    private final UserLoginViewModel userLoginViewModel;
    private final ViewManagerModel viewManagerModel;
    private final Frontend frontend;

    public UserLoginPresenter(ViewManagerModel viewManagerModel,
                              UserLoginViewModel userLoginViewModel,
                              Frontend frontend) {
        this.viewManagerModel = viewManagerModel;
        this.userLoginViewModel = userLoginViewModel;
        this.frontend = frontend;
    }

    @Override
    public void prepareSuccessView(UserLoginOutputData response) {
    	System.out.println("[UserLoginPresenter] success userType = " + response.getUserType());
        // 清空登录状态
        userLoginViewModel.setState(new UserLoginState());

        // 跳转 Dashboard（Admin / Staff / User）
//        frontend.showDashboard(response.getUserType());
        // 显示 Dashboard
        frontend.setVisible(true);  // ★ 关键！！！没有这行看不到任何变化
        frontend.showDashboard(response.getUserType());

        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        UserLoginState loginState = userLoginViewModel.getState();
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
