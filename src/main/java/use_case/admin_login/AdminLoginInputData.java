package use_case.admin_login;

/**
 * Input data for the admin login use case.
 */
public class AdminLoginInputData {
    private final String password;

    public AdminLoginInputData(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
