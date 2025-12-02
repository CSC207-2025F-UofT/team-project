package use_case.user_login;

public class UserLoginInputData {
    private final String userType;
    private final String username;
    private final String password;

    public UserLoginInputData(String userType, String username, String password) {
        this.userType = userType;
        this.username = username;
        this.password = password;
    }

    String getUserType() {
        return userType;
    }

    String getUsername() {
        return username;
    }

    String getPassword() {
        return password;
    }

}
