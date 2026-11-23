package interface_adapter.admin_signup;

import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;
import use_case.admin_signup.AdminSignupOutputBoundary;
import use_case.admin_signup.AdminSignupOutputData;

/**
 * The Presenter for the Admin Signup Use Case.
 */
public class AdminSignupPresenter implements AdminSignupOutputBoundary {

    private final AdminSignupViewModel adminSignupViewModel;
    private final LoginViewModel loginViewModel;
    private final ViewManagerModel viewManagerModel;

    public AdminSignupPresenter(ViewManagerModel viewManagerModel,
                                AdminSignupViewModel adminSignupViewModel,
                                LoginViewModel loginViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.adminSignupViewModel = adminSignupViewModel;
        this.loginViewModel = loginViewModel;
    }

    @Override
    public void prepareSuccessView(AdminSignupOutputData response) {
        // On success, switch to the login view.
        final LoginState loginState = loginViewModel.getState();
        loginState.setEmail(response.getEmail());
        loginViewModel.notifyStateChanged();

        switchToLoginView();
    }

    @Override
    public void prepareFailView(String error) {
        final AdminSignupState signupState = adminSignupViewModel.getState();
        signupState.setSignupError(error);
        adminSignupViewModel.notifyStateChanged();
    }

    @Override
    public void switchToLoginView() {
        adminSignupViewModel.setState(new AdminSignupState());
        adminSignupViewModel.notifyStateChanged();
        viewManagerModel.setActiveView(LoginViewModel.VIEW_NAME);
    }
}
