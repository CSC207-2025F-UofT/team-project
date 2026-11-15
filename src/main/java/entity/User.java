package entity;

/**
 * An entity representing a user, which has a username and password.
 */

public class User {

    private final String name;
    private final String password;

    /**
     * Creates a new user with given name and password. Both must be nonempty.
     * @param name the username
     * @param password the password
     * @throws IllegalArgumentException if the password or name are empty
     */

    public User(String name, String password) {
        if ("".equals(name)){
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if ("".equals(password)){
            throw new IllegalArgumentException("Password cannot be empty");
        }
        this.name = name;
        this.password = password;
    }

    public String getName(){
        return this.name;
    }

    public String getPassword(){
        return this.password;
    }
}
