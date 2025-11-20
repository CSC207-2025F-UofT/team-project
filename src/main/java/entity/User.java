package entity;

/**
 * A simple entity representing a user. Users have a username and password..
 */
public class User {

    private final String email;
    private String name;
    private String password;

    /**
     * Creates a new user with the given non-empty name and non-empty password.
     * @param email the email
     * @param name the username
     * @param password the password
     * @throws IllegalArgumentException if the email, password, or name are empty
     */
    public User(String email, String name, String password) {
        if ("".equals(email)) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if ("".equals(name)) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if ("".equals(password)) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String username) {
        this.name = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String passwordHash) {
        this.password = passwordHash;
    }

}
