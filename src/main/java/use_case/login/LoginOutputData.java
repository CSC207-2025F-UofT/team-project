package use_case.login;

public class LoginOutputData {
    private final String username;
    private final String role;
    public LoginOutputData(String username, String role) {
        this.username = username;
        this.role = role;
    }
    public String getUsername() { return username; }
    public String getRole() { return role; }
}
