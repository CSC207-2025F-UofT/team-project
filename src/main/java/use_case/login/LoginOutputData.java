package use_case.login;

import Entity.User.Role;

/**
 * Output Data for the Login Use Case.
 * Includes the role so the presenter can open the right home screen.
 */
public class LoginOutputData {

    private final String username;
    private final String id;
    private final Role role;

    public LoginOutputData(String username, String id, Role role) {
        this.username = username;
        this.id = id;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getId() {
        return id;
    }

    public Role getRole() {
        return role;
    }
}
