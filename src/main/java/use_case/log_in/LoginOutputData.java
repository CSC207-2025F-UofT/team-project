package use_case.log_in;

public class LoginOutputData {
    private final String username;

    public LoginOutputData(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }
}
