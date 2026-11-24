package interface_adapter.logged_in;

/**
 * Who is logged in right now and as what role.
 */
public class LoggedInState {
    public static final String ROLE_ADOPTER = "Adopter";
    public static final String ROLE_ADMIN = "Admin";

    private String username = "";
    private String role = "";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
