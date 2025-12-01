package use_case.user_login;

public class UserLoginInputData {
    private final String username;
    private final String password;

    public UserLoginInputData(String username, String password) {
        this.username = username;
        this.password = password;
    }

    String getUsername() {
        return username;
    }

    String getPassword() {
        return password;
    }

}
