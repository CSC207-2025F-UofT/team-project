package use_case.signup;

public class SignUpInputData {
    private final String userName;
    private final String password;
    private final String repeatPassword;

    public SignUpInputData(String userName, String password,
                           String repeatPassword) {
        this.userName = userName;
        this.password = password;
        this.repeatPassword = repeatPassword;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }
}
