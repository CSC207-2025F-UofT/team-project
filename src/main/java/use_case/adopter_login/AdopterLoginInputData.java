package use_case.adopter_login;

/**
 * Input data for the adopter login use case.
 */
public class AdopterLoginInputData {
    private final String username;
    private final String password;

    public AdopterLoginInputData(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
