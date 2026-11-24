package interface_adapter.admin_login;

import interface_adapter.ViewModel;

/**
 * View model for the admin login screen.
 */
public class AdminLoginViewModel extends ViewModel<AdminLoginState> {
    public static final String VIEW_NAME = "admin login";

    public AdminLoginViewModel() {
        super(VIEW_NAME);
        setState(new AdminLoginState());
    }
}
