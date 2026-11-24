package UseCases.SignUp;

import BoundaryData.InputData;

public class SignUpInputData extends InputData{
    private final String username;
    private final String password;
    private final String confirmPassword;

    public SignUpInputData(String username, String password, String confirmPassword) {
        super();
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    String getUsername() {
        return username;
    }

    String getPassword() {
        return password;
    }

    public String getPasswordConfirm() {
        return confirmPassword;
    }
}
