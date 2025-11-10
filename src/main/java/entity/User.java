package entity;

public class User {
    private final String name;
    private final String password;

    /*
    Creates a new user with the given name and password,
    throws IllegalArgumentException if username or password is empty
     */
    public User(String name, String password) {
        if ("".equals(name)) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if ("".equals(password)) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() { return password; }
}
