package use_case.login;

public class LoginInputData {
    private final String username;
    private final boolean researcher;
    public LoginInputData(String username, boolean researcher) {
        this.username = username;
        this.researcher = researcher;
    }
    public String getUsername() { return username; }
    public boolean isResearcher() { return researcher; }
}
