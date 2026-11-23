package use_case.signup;

public class SignupOutputData {
    private final String username;
    private final String role;
    public SignupOutputData(String username, String role) {
        this.username = username;
        this.role = role;
    }
    public String getUsername() { return username; }
    public String getRole() { return role; }
}
