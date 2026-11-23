package use_case.signup;

public class SignupInputData {
    private final String username;
    private final boolean researcher;
    public SignupInputData(String username, boolean researcher) {
        this.username = username;
        this.researcher = researcher;
    }
    public String getUsername() { return username; }
    public boolean isResearcher() { return researcher; }
}
