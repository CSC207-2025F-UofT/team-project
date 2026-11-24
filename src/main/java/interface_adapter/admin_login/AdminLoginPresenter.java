package interface_adapter.admin_login;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import use_case.admin_login.AdminLoginOutputBoundary;

/**
 * On success: goes to the logged-in screen as the admin.
 * On failure: shows the error on the admin login screen.
 */
public class AdminLoginPresenter implements AdminLoginOutputBoundary {
    private final ViewManagerModel viewManagerModel;
    private final AdminLoginViewModel adminLoginViewModel;
    private final LoggedInViewModel loggedInViewModel;

    public AdminLoginPresenter(ViewManagerModel viewManagerModel,
                               AdminLoginViewModel adminLoginViewModel,
                               LoggedInViewModel loggedInViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.adminLoginViewModel = adminLoginViewModel;
        this.loggedInViewModel = loggedInViewModel;
    }

    @Override
    public void prepareSuccessView() {
        final LoggedInState loggedInState = loggedInViewModel.getState();
        loggedInState.setUsername("Admin");
        loggedInState.setRole(LoggedInState.ROLE_ADMIN);
        loggedInViewModel.firePropertyChanged();

        adminLoginViewModel.getState().setError(null);
        adminLoginViewModel.firePropertyChanged();

        viewManagerModel.switchTo(loggedInViewModel.getViewName());
    }

    @Override
    public void prepareFailView(String errorMessage) {
        adminLoginViewModel.getState().setError(errorMessage);
        adminLoginViewModel.firePropertyChanged();
    }
}
