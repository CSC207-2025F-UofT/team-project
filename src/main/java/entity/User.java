package entity;

public abstract class User {

    private String username;
    private String password;
    private int uniqueUserId;

    public User(String username, String password, int uniqueUserId) {
        this.username = username;
        this.password = password;
        this.uniqueUserId = uniqueUserId;
    }
}
