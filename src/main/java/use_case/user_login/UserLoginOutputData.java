package use_case.user_login;

public class UserLoginOutputData {

    private final String username;
    private final String userType;

    public UserLoginOutputData(String username, String userType) {
        this.username = username;
        this.userType = userType;
    }

    public String getUsername() {
        return username;
    }

    public String getUserType() {
        return userType;
    }
}