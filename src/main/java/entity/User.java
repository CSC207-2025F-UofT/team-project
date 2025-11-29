package entity;
/*
Add an attribute: user_type: String, its equal to user or admin,
input data depend on the page its login, access to different DB, and show different logged in view.

 */
public class User {

    private final String name;
    private final String password;
    private final String userType;

    /**
     * Creates a new user with the given non-empty name and non-empty password.
     * @param name the username
     * @param password the password
     * @throws IllegalArgumentException if the password or name are empty
     */
    public User(String name, String password, String userType) {
        if ("".equals(name)) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if ("".equals(password)) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if ("".equals(userType)) {
            throw new IllegalArgumentException("userType cannot be empty");
        }
        this.name = name;
        this.password = password;
        this.userType = userType;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
    
    public String getUserType() { 
    	return userType; 
    }

}