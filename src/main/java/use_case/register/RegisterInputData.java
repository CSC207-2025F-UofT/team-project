package use_case.register;

public class RegisterInputData {

    private final String email;
    private final String username;
    private final String password;
    private final String confirmPassword;

    public RegisterInputData(String email, String username, String password, String confirmPassword) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    String getEmail() {
        return email;
    }

    String getUsername() {
        return username;
    }

    String getPassword() {
        return password;
    }

    String getConfirmPassword() {
        return confirmPassword;
    }
}
