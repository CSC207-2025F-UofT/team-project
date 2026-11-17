package Entities;

public class User {
    private String username;
    private String password;
    private UserProfile profile;

    private User(String username, String password, UserProfile profile) {
        this.username = username;
        this.password = password;
        this.profile = profile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserProfile getUserProfile() {
        return profile;
    }

    public static User UserFactory(String username, String password, UserProfile profile) throws IllegalArgumentException { 
        if (username == null) {
            throw new IllegalArgumentException("Username is required.");
        }
        if (password == null) {
            throw new IllegalArgumentException("Password is required.");
        }

        return new User(username, password, profile);
    }

}