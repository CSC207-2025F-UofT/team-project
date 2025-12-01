package use_case.user_login;

public class UserLoginOutputData {

    private final String username;

    public UserLoginOutputData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}