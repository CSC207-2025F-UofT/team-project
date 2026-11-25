package use_case.signup;

public class SignUpOutputData {
    private final String userName;

    public SignUpOutputData(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
}
