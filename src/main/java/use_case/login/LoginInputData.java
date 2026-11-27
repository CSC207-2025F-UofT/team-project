package use_case.login;

/**
 * The Input Data for the Login use case.
 */
public class LoginInputData {

    private final String username;
    private final String password;

    /**
     * Creates the input data.
     *
     * @param username the username typed by the user
     * @param password the password typed by the user
     */
    public LoginInputData(String username, String password) {
        this.username = username;
        this.password = password;
    }

    String getUsername() {
        return username;
    }

    String getPassword() {
        return password;
    }
}
