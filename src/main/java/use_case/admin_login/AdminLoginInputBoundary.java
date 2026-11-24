package use_case.admin_login;

/**
 * Input boundary for the admin login use case.
 */
public interface AdminLoginInputBoundary {
    void execute(AdminLoginInputData inputData);
}
