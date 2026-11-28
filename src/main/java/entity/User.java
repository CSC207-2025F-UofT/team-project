package entity;

public class User {
    private String username;
    private String passwordHash;
    private String email;
    private String salt;

    public User(String username, String passwordHash, String email, String salt) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.salt = salt;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public String getSalt() {
        return salt;
    }
}
