package entity;

public abstract class User {

    private String username;
    private String password;
    private int uniqueUserId;

    // Constructor
    public User(String username, String password, int uniqueUserId) {
        this.username = username;
        this.password = password;
        this.uniqueUserId = uniqueUserId;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getUniqueUserId() {
        return uniqueUserId;
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUniqueUserId(int uniqueUserId) {
        this.uniqueUserId = uniqueUserId;
    }
}
