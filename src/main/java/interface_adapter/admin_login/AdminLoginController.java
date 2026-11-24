package interface_adapter.admin_login;

import use_case.admin_login.AdminLoginInputBoundary;
import use_case.admin_login.AdminLoginInputData;

/**
 * Turns the admin login form's input into a call to the admin login use case.
 */
public class AdminLoginController {
    private final AdminLoginInputBoundary interactor;

    public AdminLoginController(AdminLoginInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String password) {
        interactor.execute(new AdminLoginInputData(password));
    }
}
