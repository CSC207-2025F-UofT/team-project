package use_case.user_login;

import entity.User;

public class UserLoginOutputData {

    private final String username;
    private final String userType;

    private final User user; 

    public UserLoginOutputData(String username, String userType, User user) {
        this.username = username;
        this.userType = userType;
        this.user = user;
    }

    public String getUsername() {
        return username;
    }

    public String getUserType() {
        return userType;
    }

    public User getUser() { 
        return user;
    }
}
